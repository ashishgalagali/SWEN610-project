<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <title>${title} | Web Checkers</title>
    <link rel="stylesheet" href="/css/style.css">
    <link rel="stylesheet" href="/css/game.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <link rel="shortcut icon" href="#" />

    <script>
        window.gameState = {
            'player': {
                'name': '${playerName}',
                'color': '${playerColor}',
                'isMyTurn': ${isMyTurn?c}
            },
            'opponent': {
                'name': '${opponentName}',
                'color': '${opponentColor}',
                'isMyTurn': ${(!isMyTurn)?c}
            }
        };
    </script>
</head>
<body>
<div class="page">
    <h1>Web Checkers</h1>

    <div class="navigation">
        <#if currentPlayer??>
            <a href="/">my home</a> |
            <a href="/logout">sign out [${playerName}]</a>
        <#else>
            <a href="/signin">sign in</a>
        </#if>
    </div>

    <div class="body">
        <#if ended>
            <div id="message" class="${message.type}">${message.text}</div>
            Your points: ${points}
        <#else>
            <form id="gameForm" action="/submitTurn" method="POST">
                <p>
                    You are playing a game of checkers with ${opponentName}.
                    <#if isMyTurn>
                        It's your turn.  Move your piece and click the Submit link.
                        If you want to erase your move click the Reset link.
                    <#else>
                        It's ${opponentName}'s turn.  The page will refresh periodically
                        and you will be informed when it is your turn.
                    </#if>
                </p>

                <div>
                    <div id="game-controls">

                        <fieldset id="game-info">
                            <legend>Info</legend>

                            <#if message??>
                                <div id="message" class="${message.type}">${message.text}</div>
                            <#else>
                                <div id="message" class="info" style="display:none">
                                    <!-- keep here for Client-side messages -->
                                </div>
                            </#if>

                            <div>
                                <table data-color='RED'>
                                    <tr>
                                        <td><img src="../img/single-piece-red.svg"/></td>
                                        <td class="name">Red</td>
                                    </tr>
                                </table>
                                <table data-color='WHITE'>
                                    <tr>
                                        <td><img src="../img/single-piece-white.svg"/></td>
                                        <td class="name">White</td>
                                    </tr>
                                </table>
                            </div>
                        </fieldset>

                        <fieldset id="game-toolbar">
                            <legend>Controls</legend>
                            <div class="toolbar">
                                <a href="#" id="submitLink" enabled=enabled
                                   title="Commit your current turn to the server.">
                                    Submit turn
                                </a>
                                <a href="/resignGame" id="resignLink" enabled=enabled
                                   title="End the game by resigning.">
                                    Resign from game
                                </a>
                            </div>
                        </fieldset>

                    </div>

                    <div class="game-board">
                        <table id="game-board">
                            <tbody>
                            <#list board.iterator() as row>
                                <tr data-row="${row.index}">
                                    <#list row.iterator() as square>
                                        <td data-cell="${square.cellIdx}"
                                                <#if square.isValid() >
                                                    class="Square"
                                                </#if>
                                        >
                                            <#if square.piece??>
                                                <div class="Piece"
                                                     id="piece-${row.index}-${square.cellIdx}"
                                                     data-type="${square.piece.type}"
                                                     data-color="${square.piece.color}">
                                                </div>
                                            </#if>
                                        </td>
                                    </#list>
                                </tr>
                            </#list>
                            </tbody>
                        </table>
                    </div>
                </div>
            </form>
        </#if>
    </div>
</div>

<audio id="audio" src="http://www.soundjay.com/button/beep-07.mp3" autostart="false"></audio>

<script data-main="js/game/index" src="js/require.js"></script>

</body>
</html>
