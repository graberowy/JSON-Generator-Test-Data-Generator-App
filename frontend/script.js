document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("generator-form");
    const baseJsonTextarea = document.getElementById("baseJson");
    const formattedJsonOutput = document.getElementById("formatted-json");
    const formatButton = document.getElementById("formatJsonBtn");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        const filename = form.filename.value.trim();
        const count = parseInt(form.count.value.trim());
        const baseJsonText = baseJsonTextarea.value.trim();

        // Walidacja pól
        if (!filename || isNaN(count) || count < 1 || !baseJsonText) {
            alert("Complete all fields correctly.");
            return;
        }

        let baseJsonObject;
        try {
            baseJsonObject = JSON.parse(baseJsonText);
        } catch (parseError) {
            alert("Invalid JSON! Check syntax.");
            return;
        }

        const payload = {
            filename: filename.endsWith(".json") ? filename : `${filename}.json`,
            count: count,
            baseJson: baseJsonObject,
            randomize: form.randomize.checked
        };

        try {
            const res = await fetch('/generate-json', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });

            if (!res.ok) throw new Error("Error while generating file");

            const blob = await res.blob();
            const a = document.createElement('a');
            a.href = window.URL.createObjectURL(blob);
            a.download = payload.filename;
            a.click();
        } catch (err) {
            alert("Error: " + err.message);
        }
    });

    formatButton.addEventListener("click", function () {
        try {
            const obj = JSON.parse(baseJsonTextarea.value);
            const formatted = JSON.stringify(obj, null, 2);
            baseJsonTextarea.value = formatted;
            formattedJsonOutput.textContent = formatted;
        } catch (e) {
            alert("Invalid JSON - cannot be formatted.");
            formattedJsonOutput.textContent = "(incorrect JSON)";
        }
    });
});
