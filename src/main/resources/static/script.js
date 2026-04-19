let chartInstance = null;

function upload() {

    let file = document.getElementById("fileInput").files[0];
    let role = document.getElementById("role").value;

    if (!file) {
        alert("Upload resume first!");
        return;
    }

    let formData = new FormData();
    formData.append("file", file);
    formData.append("role", role);

    fetch("/analyze", {
        method: "POST",
        body: formData
    })
    .then(res => res.json())
    .then(data => {

        console.log("API RESPONSE:", data);

        let score = data.score || 0;

        // 🎨 Color logic
        let color = "red";
        if (score >= 70) color = "green";
        else if (score >= 40) color = "orange";

        // Score display
        let scoreEl = document.getElementById("scoreText");
        scoreEl.innerText = "ATS Score: " + score;
        scoreEl.style.color = color;

        // Progress bar
        let bar = document.getElementById("progressBar");
        bar.style.width = score + "%";
        bar.style.background = color;

        // Skills
        let matched = data.skills?.matched || [];
        let missing = data.skills?.missing || [];

        document.getElementById("matched").innerText =
            matched.length ? matched.join(", ") : "None";

        document.getElementById("missing").innerText =
            missing.length ? missing.join(", ") : "None";

        // 🔥 AI FEEDBACK CLEANING
        let feedbackText = data.feedback;

        try {
            let parsed = JSON.parse(feedbackText);

            // Try extracting actual text safely
            feedbackText =
                parsed.output?.[0]?.content?.[0]?.text ||
                JSON.stringify(parsed, null, 2);

        } catch (e) {
            // fallback (already plain text)
        }

        document.getElementById("feedback").innerText =
            feedbackText || "No feedback available";

        // Chart
        createChart(matched.length, missing.length);

    })
    .catch(err => {
        console.error(err);
        alert("Error connecting to backend");
    });
}


// 📊 Chart
function createChart(matched, missing) {

    let ctx = document.getElementById("chart");

    if (chartInstance) chartInstance.destroy();

    chartInstance = new Chart(ctx, {
        type: "doughnut",
        data: {
            labels: ["Matched", "Missing"],
            datasets: [{
                data: [matched, missing],
                backgroundColor: ["green", "red"]
            }]
        }
    });
}