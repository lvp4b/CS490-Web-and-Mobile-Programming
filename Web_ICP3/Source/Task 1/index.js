let score = 0;
let cpu_score = 0;

const onclick = ({currentTarget}) => {
    // picks a random choice for the cpu
    const cpu = ["rock", "paper", "scissors"][Math.floor(Math.random() * 3)];
    document.getElementById("cpu_option").src = `images/${cpu}.png`;

    // updates score
    const winsAgainst = {rock: "scissors", paper: "rock", scissors: "paper"};
    const player = currentTarget.id;
    if (player === cpu) {
    } else if (winsAgainst[player] === cpu) {
        score++;
    } else {
        cpu_score++;
    }

    document.getElementById("option").src = `images/${player}.png`;
    document.getElementById("score").innerText = score.toString();
    document.getElementById("cpu_score").innerText = cpu_score.toString();

    // reset
    setTimeout(() => {
        document.getElementById("option").src = "images/rocking.gif";
        document.getElementById("cpu_option").src = "images/rocking.gif";
    }, 1500);
};

["rock", "paper", "scissors"].forEach(id => document.getElementById(id).addEventListener("click", onclick));
