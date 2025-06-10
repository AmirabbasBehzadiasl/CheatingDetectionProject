from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer, util

app = Flask(__name__)

<<<<<<< HEAD
model = SentenceTransformer('all-MiniLM-L6-v2')
=======
 model = SentenceTransformer('all-MiniLM-L6-v2')
>>>>>>> pythonService

@app.route('/similarity', methods=['POST'])
def get_similarity():
    try:
        data = request.get_json()
        if not data or 'text1' not in data or 'text2' not in data:
            return jsonify({'error': 'Missing text1 or text2 in request'}), 400

        text1 = data['text1']
        text2 = data['text2']

        embedding1 = model.encode(text1, convert_to_tensor=True)
        embedding2 = model.encode(text2, convert_to_tensor=True)

        similarity = util.cos_sim(embedding1, embedding2)[0][0].item()

        similarity = round(similarity, 2)

        return jsonify({'similarity': similarity}), 200

    except Exception as e:
        return jsonify({'error': f'Error processing request: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)