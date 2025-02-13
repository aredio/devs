const pokemonList = document.getElementById('pokemonList')

const loadMoreButton = document.getElementById('loadMoreButton')
const maxRecords = 386 
const limit = 50
let offset = 0

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
        <a href="https://pokemondb.net/pokedex/${pokemon.number}" target="_blank">
            <button class="details-button">+Info</button>
        </a>
    </li>
    `
}


function loadPokemonItens(offset,limit) {
    pokeApi.getPokemons(offset,limit).then((pokemons = []) => {
        pokemonList.innerHTML += pokemons.map(convertPokemon).join('')
        
    })
}
loadPokemonItens(offset,limit)

loadMoreButton.addEventListener('click', () => {
    offset += limit

    const qtdRecordWithNextPage = offset + limit

    if (qtdRecordWithNextPage >= maxRecords) {
        const newLimit = maxRecords - offset
        loadPokemonItens(offset, newLimit)
        
        loadMoreButton.parentElement.removeChild(loadMoreButton)

    } else {
    loadPokemonItens(offset, limit)
    }
})