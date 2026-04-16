let resultText = "";
let pieChart = null;

function upload() {

    let file = document.getElementById("fileInput").files[0];
    let role = document.getElementById("role").value;

    if (!file) {
        alert("Upload resume!");
        return;
    }

    document.getElementById("scoreText").innerText = "Analyzing...";

    let formData = new FormData();
    formData.append("file", file);
    formData.append("role", role);

    fetch("/analyze", {
        method: "POST",
        body: formData
    })
    .then(res => res.json())
    .then(data => {

        let score = data.score ?? 0;

        // 🎨 COLOR LOGIC
        let color = "red";
        if (score >= 70) color = "green";
        else if (score >= 40) color = "orange";

        let scoreEl = document.getElementById("scoreText");
        scoreEl.innerText = "ATS Score: " + score;
        scoreEl.style.color = color;

        // 📊 Progress bar
        let bar = document.getElementById("progressBar");
        bar.style.width = score + "%";
        bar.style.background = color;

        // Feedback
        document.getElementById("feedback").innerText = data.feedback;

        let matched = data.skills?.matched || [];
        let missing = data.skills?.missing || [];

        document.getElementById("matched").innerText = matched.join(", ");
        document.getElementById("missing").innerText = missing.join(", ");

        // 📊 CHART
        createChart(matched.length, missing.length);

        // Save
        resultText =
            "Role: " + role + "\n\n" +
            "ATS Score: " + score + "\n\n" +
            "Feedback:\n" + data.feedback + "\n\n" +
            "Matched: " + matched.join(", ") + "\n\n" +
            "Missing: " + missing.join(", ");
    });
}


// 📊 Chart.js
function createChart(matched, missing) {

    let ctx = document.getElementById("chart");

    if (pieChart) pieChart.destroy();

    pieChart = new Chart(ctx, {
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


// 📥 Download as PDF
function download() {

    if (!resultText) {
        alert("Analyze first!");
        return;
    }

    const { jsPDF } = window.jspdf;
    let doc = new jsPDF();

    doc.text(resultText, 10, 10);
    doc.save("ATS_Report.pdf");
}