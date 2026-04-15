const board = document.getElementById('board');

let solved = null;
let original = null;
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
            if (original?.[r]?.[c] !== 0 && original){
                input.value = val;
                input.classList.add('fixed')
                input.readOnly = true;
            }
            else if (val !== 0) {
                input.value = val;
                input.classList.add('player');
            }
            board.appendChild(input);
        }
    }
}

async function loadPuzzle(){
    const res = await fetch('api/dodoku/new')
    const data = await res.json()
    puzzle   = data.puzzle;
    original = data.puzzle.map(row => [...row])
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
    original[r][c] = solved[r][c]
    renderBoard(puzzle)
}

function revealSolution(){
    puzzle = solved;
    original = solved;
    renderBoard(puzzle)
}

async function checkLegality(r, c, v){
    const res = await fetch("http://localhost:8080/api/dodoku/legality", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ board: puzzle, row: r, col: c, val: v })
    })
    const data = await res.json()
    return data.legal
}

function updateInstructions(cell){
    const instructions = document.getElementById('instructions');
    if (!puzzle) {
        instructions.textContent = 'Click "New puzzle" to start';
        return;
    }
    if (!cell || cell.classList.contains('fixed')) {
        instructions.textContent = '';
        return;
    }
    if (cell.classList.contains('player')) {
        instructions.textContent = 'Press Backspace to delete';
    } else if (cell.value) {
        instructions.textContent = 'Press Enter to confirm';
    } else {
        instructions.textContent = 'Type a value (1-9) and press Enter';
    }
}
function updateStatus(){
    const status = document.getElementById('status')
    let filledCells = 0;

    if(!puzzle){
        status.textContent = ''
    }
}

board.addEventListener('input',(e) => {
    if (!puzzle) {
        e.target.value = '';
        updateInstructions(e.target);
        return;
    }
    if (!/^[1-9]$/.test(e.target.value)) {
        e.target.value = '';
    }
    updateInstructions(e.target)
});

board.addEventListener('keydown', async (e) => {
    const row = Number(e.target.dataset.row)
    const col = Number(e.target.dataset.col);
    if (e.key === 'Enter') {
        let val = e.target.value
        if (!val) return;
        const inputVal = Number(val);
        const isLegal = await checkLegality(row, col, inputVal);
        if (isLegal){
            puzzle[row][col] = inputVal;
        }
        else {
            e.target.value = '';
        }
        renderBoard(puzzle);
        const cell = document.querySelector(`input[data-row="${row}"][data-col="${col}"]`);
        cell.classList.add(isLegal ? 'valid' : 'invalid');
        setTimeout(() => cell.classList.remove('valid', 'invalid'), 2000);
    }
    else if (e.key === 'Backspace'){
        if (e.target.classList.contains('fixed')) return;
        puzzle[row][col] = 0;
        e.target.value = '';
        e.target.classList.remove('player');
        renderBoard(puzzle);
        return;
    }
    const cell = document.querySelector(`input[data-row="${row}"][data-col="${col}"]`);
    updateInstructions(cell);
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
    updateInstructions(e.target)
});

document.addEventListener('click', (e) => {
    if (!board.contains(e.target)) {
        document.getElementById('instructions').textContent = '';
    }
});

document.getElementById('new').onclick = loadPuzzle;
document.getElementById('hint').onclick = giveHint;
document.getElementById('giveup').onclick = revealSolution;

renderBoard();