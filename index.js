class Pessoa {
    nome;
    peso;
    altura;
    constructor(nome, peso, altura) {
        this.nome = nome;
        this.peso = peso;
        this.altura = altura;
    }   
}

function calcularImc(pessoa) {
    const imc = pessoa.peso / (pessoa.altura * pessoa.altura);
    console.log(`O IMC de ${pessoa.nome} é ${imc}`);
    if (imc < 18.5) {
        console.log(`Essa pessoa está abaixo do peso`);
    } else if (imc >= 18.5 && imc < 25) {
        console.log(`IMC dentro do normal`);
    } else if (imc >= 25 && imc < 30) {
        console.log(`Essa pessoa está acima do peso`);
    } else if (imc >= 30 && imc < 40) {
        console.log(`Essa pessoa está obesa`);
    } else {
        console.log('Essa pessoa está com obesidade mórbida');
    }
}

const p1 = new Pessoa('Heloísa', 85, 1.75);
calcularImc(p1);