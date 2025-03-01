
async function fetchProfileData() {
    const url = 'https://github.com/aredio/devs/raw/refs/heads/main/folio/data/profile.json';
    const response = await fetch(url)
    const profileData = await response.json()
    return profileData
}
