
function convertPokemon(pokemon) { 
    return `
    <li class="pokemon ${pokemon.type}">
        <span class = "number">#${pokemon.number}</span>
        <span class = "name">${pokemon.name}</span>
        <div class="info">
            <ol class="types">
            ${pokemon.types
                .map(
                  (type) =>
                    `<li class="type ${type}">${type}</li>`
                )
                .join("")}
            </ol>
            <img src="${pokemon.image}" alt="${pokemon.name}">
        </div>
        
    </li>
    `
}

const pokemonList = document.getElementById('pokemonList')

pokeApi.getPokemons().then((pokemons = []) => {
    pokemonList.innerHTML += pokemons.map(convertPokemon).join('')
    
})