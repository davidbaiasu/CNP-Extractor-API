async function parseCNP() {

    const cnpValue = document.getElementById('cnpInput').value;
    const resultBox = document.getElementById('result');

    if (!cnpValue) {
        alert("Please enter a CNP first!");
        return;
    }

    try {
        const response = await fetch(`/api/parse?number=${cnpValue}`);
        
        const data = await response.json();

        if (data.valid) {
            document.getElementById('resGender').innerText = data.gender;
            document.getElementById('resDate').innerText = data.birthDate;
            document.getElementById('resCounty').innerText = data.countyName;
            document.getElementById('resValid').innerText = "✅ Valid CNP";
			document.getElementById('resCode').innerText = data.serialCode;
            
            resultBox.style.display = 'block';
        } else {
            alert("This CNP is not valid according to the logic!");
            resultBox.style.display = 'none';
        }
    } catch (error) {
        console.error("Error connecting to the API:", error);
        alert("Could not connect to the server. Is the Java app running?");
    }
}