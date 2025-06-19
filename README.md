# 📦 JSON Generator – Test Data Generator App

This application allows for quick and convenient generation of `.json` files based on a template structure. The user can choose whether the generated data should be copies of the base object or filled with randomized values.

---

## ✨ Features

- 📝 Paste a base JSON structure and generate any number of objects
- 🔢 Auto-increment of the `id` field in generated objects
- 🔀 Optional random data generation (`randomize`)
- 💾 Download the generated data as a `.json` file
- 👀 Preview formatted JSON after clicking "Format JSON"
- ⚡ Lightweight frontend (HTML + JS, no frameworks)
- 🔧 Backend built with plain `Java + HttpServer`
- 🐳 Docker-ready for easy containerized execution

---

## 🚀 Running Locally

### Requirements:

- Java 17+ (e.g. OpenJDK 17 or 21)
- Maven 3.6+
- Docker (optional, for containerization)

---

### 🔧 1. Build the application

```bash
mvn clean package
```

After building, the `.jar` file will appear in the `target/` directory.

---

### 🐳 2. Build and run with Docker

```bash
docker build -t json-generator .
docker run -p 8080:8080 json-generator
```

---

### 🌐 3. Access the app

Once running, open in your browser:

```
http://localhost:8080
```

---

## 🖥️ User Interface

The form includes:

- 🗂 **File name** – name of the resulting JSON file
- 🔢 **Record count** – number of records to generate
- 🧪 **Checkbox "Generate random data"** – if checked, each object instance will be filled with random values (including nested fields)
- 📋 **Textarea** – for pasting the base JSON structure
- 🔘 **Format JSON** – format and preview the JSON
- 📥 **Generate and download** – generate and download the `.json` file

---

## 📂 Project Structure

```
json-generator/
├── frontend/
│   ├── index.html
│   └── script.js
├── src/
│   └── main/java/com/code4u/JsonGeneratorServer.java
├── Dockerfile
├── pom.xml
└── README.md
```

---

## 🧠 Example Use Cases

- Testing APIs with structured JSON data
- Simulating system integrations with mock data
- Creating demo datasets for frontend development

---

## 🛠️ TODO / Future Improvements

- [ ] Support for generating multiple files at once
- [ ] History of generated files
- [ ] JSON templates for quick start
- [ ] JSON Schema validation support

---

## 👨‍💻 Author

Paweł Jankowski – 2025  
Project created for personal use and to automate test data generation.

---

## 📜 License

MIT – use it, improve it, share it! 🔓
