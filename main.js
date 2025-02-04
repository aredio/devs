const {gets,print} = require('./aux-functions.js');

//print(gets());

const numerosSorteados = [];
for (let i = 0; i < 5; i++) {
    const numeroSorteado = gets();
    numerosSorteados.push(numeroSorteado);
}

let maior = 0;

for (let i = 0; i < numerosSorteados.length; i++) {
    const numeroSorteado = numerosSorteados[i];
    if (numeroSorteado > maior) {
        maior = numeroSorteado;
    }
}

print(maior);