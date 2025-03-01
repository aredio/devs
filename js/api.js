async function fetchProfileData() {
    const proxyUrl = 'https://api.allorigins.win/get?url=';
    const targetUrl = encodeURIComponent('https://github.com/aredio/devs/raw/refs/heads/main/folio/data/profile.json');

    const response = await fetch(`${proxyUrl}${targetUrl}`);
    const data = await response.json();
    return JSON.parse(data.contents);
}
