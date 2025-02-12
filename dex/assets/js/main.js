
function convertPokemon(pokemon) { 
    return `
    <li class="pokemon">
        <span class = "number">#001</span>
        <span class = "name">${pokemon.name}</span>
        <div class="info">
            <ol class="types">
                <li class="type">Grass</li>
                <li class="type">Poison</li>
            </ol>
            <img src="https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/1.svg" alt="${pokemon.name}">
        </div>
        
    </li>
    `
}

const pokemonList = document.getElementById('pokemonList')

pokeApi.getPokemon(0, 10).then((pokemons = []) => {
    
    const newList = pokemons.map((value, index, array) => {
        return ''
    })    
    
    const listItens = []
    
    for (let i = 0; i < pokemons.length; i++) {
        const pokemon = pokemons[i];
        listItens.push(convertPokemon(pokemon))
    }
    pokemonList.innerHTML += convertPokemon(pokemon)
})
    
    // .finally(function () {
    //     console.log('done')
// })
    /**/