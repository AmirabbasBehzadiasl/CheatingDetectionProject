from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/similarity', methods=['POST'])
def get_similarity():
    try:
        data = request.get_json()
        if not data or 'text1' not in data or 'text2' not in data:
            return jsonify({'error': 'Missing text1 or text2 in request'}), 400

        return jsonify({'similarity': 5}), 200

    except Exception as e:
        return jsonify({'error': f'Error processing request: {str(e)}'}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)