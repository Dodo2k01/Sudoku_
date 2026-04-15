const board = document.getElementById('board');

let solved = null;
let puzzle = null;
let lastActiveCell = null;

function renderBoard(grid) {
    board.innerHTML = '';
    for (let r = 0; r < 9; r++) {
        for (let c = 0; c < 9; c++) {
            const input = document.createElement('input');
            input.maxLength = 1;
            input.dataset.row = r;
            input.dataset.col = c;
            const val = grid?.[r]?.[c] ?? 0;
            if (val !== 0) {
                input.value = val;
                input.classList.add('fixed');
                input.readOnly = true;
            }
            board.appendChild(input);
        }
    }
}

async function loadPuzzle(){
    const res = await fetch('api/dodoku/new')
    const data = await res.json()
    puzzle   = data.puzzle;
    solved = data.solved;
    renderBoard(puzzle)
}

function giveHint(){
    const misses = []
    for (let r = 0; r<9; r++){
        for (let c = 0; c<9;c++){
            if (solved[r][c]!== puzzle[r][c]){
                misses.push([r,c])
            }
        }
    }
    if (misses.length===0) return;
    const [r, c] = misses[Math.floor(Math.random()*misses.length)]
    puzzle[r][c] = solved[r][c]
    renderBoard(puzzle)
}

// function inputValue(){
//
// }

async function checkLegality(r, c, v){
    const res = await fetch("http://localhost:8080/api/dodoku/legality", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ board: puzzle, row: r, col: c, val: v })
    })
    const data = await res.json()
    return data.legal
}

board.addEventListener('input',(e) => {
    if (!/^[1-9]$/.test(e.target.value)) {
        e.target.value = '';
    }
});

board.addEventListener('keydown', async (e) => {
    if (e.key !== 'Enter') return;
    const val = e.target.value;
    if(!val) return;
    const row = Number(e.target.dataset.row)
    const col = Number(e.target.dataset.col);
    const inputVal = Number(val);
    const isLegal = await checkLegality(row, col, inputVal);
    if (isLegal){
        puzzle[row][col] = inputVal;
    }
    else {
        e.target.value = '';
    }
    renderBoard(puzzle)

    const cell = document.querySelector(`input[data-row="${row}"][data-col="${col}"]`);
    cell.classList.add(isLegal ? 'valid' : 'invalid');
    setTimeout(() => cell.classList.remove('valid', 'invalid'), 2000);
})

board.addEventListener('focusin', (e) => {
    const row = e.target.dataset.row;
    const col = e.target.dataset.col;
    const cellKey = `${row}-${col}`;

    if (lastActiveCell !== cellKey) {
        lastActiveCell = cellKey;
        renderBoard(puzzle);
        const cell = document.querySelector(`input[data-row="${row}"][data-col="${col}"]`);
        cell.focus();
    }
});

document.getElementById('new').onclick = loadPuzzle;
document.getElementById('hint').onclick = giveHint;
document.getElementById('check').onclick = () => alert('Not wired up yet.');

renderBoard();