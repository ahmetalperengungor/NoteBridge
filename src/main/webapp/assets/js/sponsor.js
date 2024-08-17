document.addEventListener('DOMContentLoaded', function () {
    const sponsorButton = document.getElementById('sponsor-button');
    const sponsorForm = document.getElementById('sponsor-form');
    const overlay = document.getElementById('overlay');
    const sponsorCloseBtn = document.getElementById('sponsor-close-btn');
    const sponsorSubmit = document.getElementById('sponsor-submit');
    const sponsorContentSubmit = document.getElementById('sponsor-content-submit');

    sponsorButton.addEventListener('click', (e) => {
        e.preventDefault();
        sponsorForm.style.display = 'block';
        overlay.style.display = 'block';
    });

    sponsorCloseBtn.addEventListener('click', () => {
        sponsorForm.style.display = 'none';
        overlay.style.display = 'none';
    });

    overlay.addEventListener('click', () => {
        sponsorForm.style.display = 'none';
        overlay.style.display = 'none';
    });

    sponsorSubmit.addEventListener('click', async (e) => {
        e.preventDefault();

        const companyName = document.getElementById('company-name').value;
        const price = document.getElementById('price').value;
        const fromDate = document.getElementById('from-date').value;
        //const toDate = document.getElementById('to-date').value;
        const numPosts = document.getElementById('num-posts').value;

        const sponsorData = {
            commercialName: companyName,
            price: price,
            agreementDate: fromDate,
            agreementPostCount: numPosts
        };

        try {
            const response = await makeHttpRequest('/sponsors', 'POST', sponsorData);
            if (response.status === 201) {
                alert('Sponsorship submitted successfully!');
                // Optionally, close the form and clear inputs
                sponsorForm.style.display = 'none';
                overlay.style.display = 'none';
            } else {
                alert('Failed to submit sponsorship.');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred during sponsorship submission.');
        }
    });

    // Handle sponsor content creation
    sponsorContentSubmit.addEventListener('click', (e) => {
        e.preventDefault();
        const title = document.querySelector('.create-sponsor-form-title').value;
        const description = document.querySelector('.create-sponsor-form-content').value;
        const sponsorId = document.querySelector('.create-sponsor-form-sponsorId').value;
        const files = document.getElementById('sponsor-image-upload').files;
        console.log('Files: ', files);
        if (files.length === 0) {
            alert('Please select at least one image to upload.');
            return;
        }

        saveSponsorContent({ title, description, sponsorId, files });
    });

    // Save sponsor content and update the UI
    async function saveSponsorContent(sponsorContent) {
        const files = sponsorContent.files;
        let body = {
            title: sponsorContent.title,
            description: sponsorContent.description,
            sponsorId: sponsorContent.sponsorId
        };

        const response = await makeHttpRequest('/sponsors', 'POST', body);
        // get files from a file list
        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            getBase64(file, async base64String => {
                const formData = new FormData();
                formData.append('file', file);
                makeXHRRequest('/sponsors/uploadSponsorPicture/' + response.data.id, formData);
            });
        }
        clearSponsorForm();
        const { data } = await makeHttpRequest('/sponsors');
        const sponsors = data.sort((a, b) => b.createdAt - a.createdAt);
        document.getElementById('sponsor-main').innerHTML = '';
        sponsors.forEach(displaySponsor);
        sponsorForm.style.display = 'none';
        overlay.style.display = 'none';
    }

    // Clear sponsor form fields
    function clearSponsorForm() {
        document.querySelector('.create-sponsor-form-title').value = '';
        document.querySelector('.create-sponsor-form-content').value = '';
        document.querySelector('.create-sponsor-form-sponsorId').value = '';
        document.getElementById('sponsor-image-upload').value = '';
    }


});
