let userName = '';
let currentQuestionIndex = 0;
let answers = []; // Stores objects with qnumber, description, startTime, endTime, timeSpentOnQuestion
let questionStartTimestamp; // Stores the Date object for current question start
let individualQuestionTimerInterval; // Timer for the current question
let overallQuizTimerInterval; // Timer for the entire quiz
const QUIZ_DURATION_SECONDS = 3 * 60; // 3 minutes
let overallTimeLeft = QUIZ_DURATION_SECONDS;
let fullAnalysisData = []; // Stores the full fetched cheating analysis data

const ADMIN_USERNAME_FRONTEND = "admin";
// IMPORTANT: The actual password check is now on the frontend only!
const ADMIN_PASSWORD_FRONTEND_PROMPT = "12345678"; // This is now the actual password used for frontend validation

// Base URL for your Spring Boot backend
const BASE_API_URL = 'http://localhost:8080/api/exams'; // Assuming your backend runs on port 8080

const questions = [
    "Photosynthesis uses sunlight to create food. This process is vital for plant life and ultimately for most life on Earth.",
    "Compiled languages typically run quicker than interpreted languages because their code is translated into machine code before execution.",
    "The Industrial Revolution, a period of major industrialization and innovation, originated in Great Britain during the late 18th century."
];

function toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
}

function resetQuiz() {
    userName = '';
    currentQuestionIndex = 0;
    answers = [];
    clearInterval(individualQuestionTimerInterval);
    clearInterval(overallQuizTimerInterval);
    overallTimeLeft = QUIZ_DURATION_SECONDS; // Reset overall timer
    fullAnalysisData = []; // Clear stored analysis data on quiz reset

    document.getElementById('userName').value = ''; // Clear username input
    document.getElementById('adminPassword').value = ''; // Clear password input
    document.getElementById('adminPassword').classList.add('hidden'); // Hide password field
    document.getElementById('studentName1').value = ''; // Clear filter inputs
    document.getElementById('studentName2').value = ''; // Clear filter inputs
    document.getElementById('minLevenshtein').value = ''; // Clear new filter inputs
    document.getElementById('minSentenceTransformer').value = '';
    document.getElementById('minSuspiciousTimeDiff').value = '';

    document.getElementById('welcomeScreen').classList.remove('hidden');
    document.getElementById('quizScreen').classList.add('hidden');
    document.getElementById('quizCompletedScreen').classList.add('hidden');
    document.getElementById('cheatingDetectionScreen').classList.add('hidden');
    document.getElementById('overallTimer').textContent = '03:00'; // Reset overall timer display
}

function checkAdminName() {
    const userNameInput = document.getElementById('userName');
    const adminPasswordField = document.getElementById('adminPassword');
    if (userNameInput.value.toLowerCase() === ADMIN_USERNAME_FRONTEND) {
        adminPasswordField.classList.remove('hidden');
    } else {
        adminPasswordField.classList.add('hidden');
        adminPasswordField.value = ''; // Clear password if admin name is removed
    }
}

function startQuiz() {
    userName = document.getElementById('userName').value.trim();

    if (userName === "") {
        alert("Please enter your name to start the quiz.");
        return;
    }

    // New validation for userName based on @Pattern and @NotBlank
    // This regex allows ONLY alphabet characters (both uppercase and lowercase)
    if (!/^[a-zA-Z]+$/.test(userName)) {
        alert("Please enter a name with only alphabet characters (no spaces or numbers).");
        return;
    }

    // If "admin" is entered, handle it via attemptShowCheatingAnalysis()
    if (userName.toLowerCase() === ADMIN_USERNAME_FRONTEND) {
        attemptShowCheatingAnalysis();
        return; // Stop quiz start logic
    }

    document.getElementById('welcomeScreen').classList.add('hidden');
    document.getElementById('quizScreen').classList.remove('hidden');
    document.getElementById('quizHeader').textContent = `Quiz - ${userName}`;

    answers = questions.map((q, index) => ({
        qnumber: index + 1,
        description: '', // Initialize description as empty string
        startTime: '',
        endTime: '',
        timeSpentOnQuestion: 0
    }));

    loadQuestion();
    updateProgressBar();
    createPaginationDots();
    startOverallQuizTimer();
}

// Function now handles frontend-only admin password check
async function attemptShowCheatingAnalysis() {
    const userNameInput = document.getElementById('userName');
    const adminPasswordInput = document.getElementById('adminPassword');
    const currentUserName = userNameInput.value.trim().toLowerCase();
    const currentAdminPassword = adminPasswordInput.value;

    // Only attempt admin login if the username is 'admin'
    if (currentUserName === ADMIN_USERNAME_FRONTEND) {
        if (!currentAdminPassword) {
            alert("Please enter the admin password.");
            adminPasswordInput.classList.remove('hidden'); // Ensure it's visible
            return;
        }

        // --- Frontend password check here ---
        if (currentAdminPassword === ADMIN_PASSWORD_FRONTEND_PROMPT) {
            console.log("Admin authenticated successfully on frontend.");
            // No backend login needed for analysis access in this setup
            await showCheatingAnalysis(); // Call showCheatingAnalysis without credentials
        } else {
            alert("Invalid admin password.");
            adminPasswordInput.value = ''; // Clear password on failure
        }
    } else {
        alert("Only 'admin' users can view cheating analysis directly from this button. Please enter 'admin' as your name.");
        document.getElementById('quizCompletedScreen').classList.add('hidden');
        document.getElementById('welcomeScreen').classList.remove('hidden');
    }
}

function startOverallQuizTimer() {
    clearInterval(overallQuizTimerInterval);
    overallTimeLeft = QUIZ_DURATION_SECONDS;
    updateOverallTimerDisplay();

    overallQuizTimerInterval = setInterval(() => {
        overallTimeLeft--;
        updateOverallTimerDisplay();

        if (overallTimeLeft <= 0) {
            clearInterval(overallQuizTimerInterval);
            alert("Time's up! The quiz has ended.");
            submitQuiz();
        }
    }, 1000);
}

function updateOverallTimerDisplay() {
    const minutes = Math.floor(overallTimeLeft / 60);
    const seconds = overallTimeLeft % 60;
    document.getElementById('overallTimer').textContent =
        `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
}

function loadQuestion() {
    clearInterval(individualQuestionTimerInterval); // Clear current question's timer before loading new
    if (currentQuestionIndex < questions.length) {
        document.getElementById('questionNumber').textContent = `Question ${currentQuestionIndex + 1} of ${questions.length}`;
        document.getElementById('questionText').textContent = questions[currentQuestionIndex];

        const userAnswerTextArea = document.getElementById('userAnswer');
        // Load previous answer
        userAnswerTextArea.value = answers[currentQuestionIndex].description;

        // Set startTime only if it's the first time visiting this question
        if (!answers[currentQuestionIndex].startTime) {
            answers[currentQuestionIndex].startTime = new Date().toISOString().slice(0, 19);
        }
        questionStartTimestamp = new Date(); // This is the timestamp when the question is loaded

        startIndividualQuestionTimer(); // Start individual question timer
        updatePaginationDots();

        // Manage Next/Submit button text
        if (currentQuestionIndex === questions.length - 1) {
            document.getElementById('nextButton').textContent = 'Submit Quiz';
        } else {
            document.getElementById('nextButton').textContent = 'Next';
        }

        // Manage Previous button visibility
        if (currentQuestionIndex > 0) {
            document.getElementById('prevButton').classList.remove('hidden');
        } else {
            document.getElementById('prevButton').classList.add('hidden');
        }
    } else {
        submitQuiz(); // Should be triggered by nextQuestion/overall timer
    }
}

function startIndividualQuestionTimer() {
    let secondsElapsed = answers[currentQuestionIndex].timeSpentOnQuestion; // Resume from previously recorded time
    document.getElementById('questionTimer').textContent = secondsElapsed;

    individualQuestionTimerInterval = setInterval(() => {
        secondsElapsed++;
        answers[currentQuestionIndex].timeSpentOnQuestion = secondsElapsed; // Update stored time
        document.getElementById('questionTimer').textContent = secondsElapsed;
    }, 1000);
}

function saveCurrentAnswer() {
    const userAnswerTextArea = document.getElementById('userAnswer');
    const userAnswer = userAnswerTextArea.value.trim();

    // New validation for description (answer) based on @NotBlank
    if (userAnswer === "") {
        alert("Please enter an answer for this question. Answers cannot be blank.");
    }

    answers[currentQuestionIndex].description = userAnswer;
    answers[currentQuestionIndex].endTime = new Date().toISOString().slice(0, 19);
}

function nextQuestion() {
    saveCurrentAnswer(); // Save the current answer before moving

    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        updateProgressBar();
        loadQuestion();
    } else {
        submitQuiz();
    }
}

function prevQuestion() {
    saveCurrentAnswer(); // Save the current answer before moving back

    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        updateProgressBar();
        loadQuestion();
    }
}

function updateProgressBar() {
    const progress = (currentQuestionIndex / questions.length) * 100;
    document.getElementById('progressBar').style.width = `${progress}%`;
}

function createPaginationDots() {
    const paginationDotsContainer = document.getElementById('paginationDots');
    paginationDotsContainer.innerHTML = '';
    for (let i = 0; i < questions.length; i++) {
        const dot = document.createElement('span');
        dot.classList.add('dot');
        dot.dataset.questionIndex = i; // Store index for navigation
        dot.onclick = () => goToQuestion(i); // Add click event for navigation
        if (i === currentQuestionIndex) {
            dot.classList.add('active');
        }
        paginationDotsContainer.appendChild(dot);
    }
}

function updatePaginationDots() {
    const dots = document.querySelectorAll('.dot');
    dots.forEach((dot, index) => {
        if (index === currentQuestionIndex) {
            dot.classList.add('active');
        } else {
            dot.classList.remove('active');
        }
    });
}

function goToQuestion(index) {
    saveCurrentAnswer(); // Save current answer before jumping
    currentQuestionIndex = index;
    updateProgressBar();
    loadQuestion();
}

async function submitQuiz() {
    // Ensure the last answer is saved before submission
    saveCurrentAnswer();

    // Additional check for all answers before final submission
    for (const answer of answers) {
        if (answer.description.trim() === "") {
            alert(`Question ${answer.qnumber} has not been answered. Please answer all questions.`);
            // Optionally, navigate to the unanswered question
            goToQuestion(answer.qnumber - 1);
            return; // Prevent submission
        }
    }

    clearInterval(individualQuestionTimerInterval);
    clearInterval(overallQuizTimerInterval);

    document.getElementById('quizScreen').classList.add('hidden');
    document.getElementById('quizCompletedScreen').classList.remove('hidden');
    document.getElementById('completedUserName').textContent = userName;
    document.getElementById('completedUserNameMsg').textContent = userName;

    // Filter out any potentially empty answers (less likely now with client-side validation)
    const submittedAnswers = answers.filter(answer => answer.description !== '');

    const submissionData = {
        name: userName,
        answers: submittedAnswers
    };

    console.log("Quiz Submission Data:", JSON.stringify(submissionData, null, 2));

    try {
        const response = await fetch(`${BASE_API_URL}/submit`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(submissionData),
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }
        const result = await response.text();
        console.log('Quiz submitted successfully:', result);
    } catch (error) {
        console.error('Error submitting quiz:', error);
        alert(`There was an error submitting your quiz: ${error.message}. Please try again.`);
        document.getElementById('quizCompletedScreen').classList.add('hidden');
        document.getElementById('welcomeScreen').classList.remove('hidden');
    }
}

// This function no longer receives credentials as they are not needed for backend fetch in this setup
async function showCheatingAnalysis() {
    document.getElementById('welcomeScreen').classList.add('hidden'); // Hide welcome screen
    document.getElementById('quizCompletedScreen').classList.add('hidden');
    document.getElementById('quizScreen').classList.add('hidden'); // Also hide quiz screen if active
    document.getElementById('cheatingDetectionScreen').classList.remove('hidden');

    const cheatingResultsDiv = document.getElementById('cheatingResults');
    cheatingResultsDiv.innerHTML = 'Loading cheating analysis...';

    // Clear any active timers if analysis is shown directly from welcome screen
    clearInterval(individualQuestionTimerInterval);
    clearInterval(overallQuizTimerInterval);

    try {
        // No username/password needed in query parameters for /analyze if frontend handles auth
        const response = await fetch(`${BASE_API_URL}/analyze`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }

        fullAnalysisData = await response.json(); // Store full data
        console.log('Cheating analysis received:', fullAnalysisData);
        renderCheatingAnalysis(fullAnalysisData); // Render all data initially

    } catch (error) {
        console.error('Error fetching cheating analysis:', error);
        cheatingResultsDiv.innerHTML = `<p style="color: red;">Error fetching cheating analysis: ${error.message}</p>`;
    }
}

function applyCheatingFilter() {
    const studentName1 = document.getElementById('studentName1').value.trim().toLowerCase();
    const studentName2 = document.getElementById('studentName2').value.trim().toLowerCase();
    const minLevenshtein = parseFloat(document.getElementById('minLevenshtein').value);
    const minSentenceTransformer = parseFloat(document.getElementById('minSentenceTransformer').value);
    const minSuspiciousTimeDiff = parseFloat(document.getElementById('minSuspiciousTimeDiff').value);

    let filteredAnalysis = fullAnalysisData;

    // Filter by names
    if (studentName1 || studentName2) {
        filteredAnalysis = filteredAnalysis.filter(pairAnalysis => {
            const s1 = pairAnalysis.student1.toLowerCase();
            const s2 = pairAnalysis.student2.toLowerCase();

            if (studentName1 && studentName2) {
                return (s1 === studentName1 && s2 === studentName2) ||
                       (s1 === studentName2 && s2 === studentName1);
            } else if (studentName1) {
                return s1 === studentName1 || s2 === studentName1;
            } else if (studentName2) {
                return s1 === studentName2 || s2 === studentName2;
            }
            return false;
        });
    }

    // Filter by similarity thresholds
    filteredAnalysis = filteredAnalysis.filter(pairAnalysis => {
        // Check if ALL question analyses within the pair meet the criteria
        return pairAnalysis.questionAnalyses.every(qa => {
            const levenshteinCheck = isNaN(minLevenshtein) || qa.levenshteinSimilarity >= minLevenshtein;
            const sentenceTransformerCheck = isNaN(minSentenceTransformer) || qa.sentenceTransformerSimilarity >= minSentenceTransformer;
            const suspiciousTimeDiffCheck = isNaN(minSuspiciousTimeDiff) || qa.suspiciousTimeDifference >= minSuspiciousTimeDiff;

            return levenshteinCheck && sentenceTransformerCheck && suspiciousTimeDiffCheck;
        });
    });

    renderCheatingAnalysis(filteredAnalysis);
}

function resetCheatingFilter() {
    document.getElementById('studentName1').value = '';
    document.getElementById('studentName2').value = '';
    document.getElementById('minLevenshtein').value = '';
    document.getElementById('minSentenceTransformer').value = '';
    document.getElementById('minSuspiciousTimeDiff').value = '';
    renderCheatingAnalysis(fullAnalysisData); // Show all original data
}

function highlightWords(text, wordsToHighlight) {
    if (!wordsToHighlight || wordsToHighlight.length === 0) {
        return text;
    }

    const pattern = new RegExp(`\\b(${wordsToHighlight.map(w => w.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')).join('|')})\\b`, 'gi');

    return text.replace(pattern, '<span class="highlight">$&</span>');
}

function renderCheatingAnalysis(analysisData) {
    const cheatingResultsDiv = document.getElementById('cheatingResults');
    cheatingResultsDiv.innerHTML = '';

    if (analysisData.length === 0) {
        cheatingResultsDiv.innerHTML = '<p>No cheating analysis data available for the selected filters, or no suspicious activity was detected.</p>';
        return;
    }

    analysisData.forEach(pairAnalysis => {
        const pairDiv = document.createElement('div');
        pairDiv.classList.add('cheating-pair');

        const avgLevenshtein = (pairAnalysis.questionAnalyses.reduce((sum, qa) => sum + qa.levenshteinSimilarity, 0) / pairAnalysis.questionAnalyses.length).toFixed(2);
        const avgSentenceTransformer = (pairAnalysis.questionAnalyses.reduce((sum, qa) => sum + qa.sentenceTransformerSimilarity, 0) / pairAnalysis.questionAnalyses.length).toFixed(2);
        const avgSuspiciousTimeDifference = (pairAnalysis.questionAnalyses.reduce((sum, qa) => sum + qa.suspiciousTimeDifference, 0) / pairAnalysis.questionAnalyses.length).toFixed(2);

        pairDiv.innerHTML = `
            <h3>Suspicious Pair: ${pairAnalysis.student1} and ${pairAnalysis.student2}</h3>
            <p><strong>Average Similarity Scores:</strong></p>
            <ul>
                <li>Levenshtein Similarity: ${avgLevenshtein}</li>
                <li>Sentence Transformer Similarity: ${avgSentenceTransformer}</li>
                <li>Suspicious Time Difference: ${avgSuspiciousTimeDifference}</li>
            </ul>
        `;

        pairAnalysis.questionAnalyses.forEach(qa => {
            const qAnalysisDiv = document.createElement('div');
            qAnalysisDiv.classList.add('question-analysis');

            const highlightedAnswer1 = highlightWords(qa.answerStudentOne, qa.similarWords);
            const highlightedAnswer2 = highlightWords(qa.answerStudentTwo, qa.similarWords);

            qAnalysisDiv.innerHTML = `
                <h4>Question ${qa.qnumber}</h4>
                <p><strong>${pairAnalysis.student1}'s Answer:</strong> ${highlightedAnswer1}</p>
                <p><strong>${pairAnalysis.student2}'s Answer:</strong> ${highlightedAnswer2}</p>
                <p>Similarity Scores - Levenshtein: ${qa.levenshteinSimilarity.toFixed(2)}, Sentence Transformer: ${qa.sentenceTransformerSimilarity.toFixed(2)}</p>
                <p>Suspicious Time for ${pairAnalysis.student1}: ${qa.suspiciousTimeStudent1.toFixed(2)}</p>
                <p>Suspicious Time for ${pairAnalysis.student2}: ${qa.suspiciousTimeStudent2.toFixed(2)}</p>
                <p>Suspicious Time Difference: ${qa.suspiciousTimeDifference.toFixed(2)}</p>
            `;
            pairDiv.appendChild(qAnalysisDiv);
        });
        cheatingResultsDiv.appendChild(pairDiv);
    });
}