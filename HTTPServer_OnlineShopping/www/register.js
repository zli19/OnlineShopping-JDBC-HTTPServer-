// JavaScript source code

let firstInput = document.getElementById("firstInput");
let secondInput = document.getElementById("secondInput");
secondInput.addEventListener("blur", compare);

function compare() {
    if (secondInput.value != '' && secondInput.value != firstInput.value) {
        alert("doesn't match")
    }
}