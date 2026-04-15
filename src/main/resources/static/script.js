const board = document.getElementById('board');

let solved = null;
let puzzle = null;

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
                misses.push([r][c])
            }
        }
    }
    let i = Math.floor(Math.random() * misses.length)
    const cell = misses[i]
    puzzle[cell[0]][cell[1]] = solved[cell[0]][cell[1]]

}

board.addEventListener('input', (e) => {
    if (!/^[1-9]$/.test(e.target.value)) e.target.value = '';
});

document.getElementById('new').onclick = loadPuzzle;
document.getElementById('hint').onclick = giveHint;
document.getElementById('check').onclick = () => alert('Not wired up yet.');

renderBoard();