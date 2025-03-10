async function fetchProfileData() {
    const url = 'https://raw.githubusercontent.com/aredio/devs/refs/heads/gh-pages/data/profile.json';

    const fetching = await fetch(url);
    return await fetching.json();
}
