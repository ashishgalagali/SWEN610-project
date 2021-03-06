/**
 * This file contains the launch code for the Game view.
 */
var time = 59;
setInterval(function () {
    var seconds = time % 60;
    if (seconds.toString().length == 1) {
        seconds = "0" + seconds;
    }
    document.getElementById("time").innerHTML = seconds;
    time--;
    if (time == 0) {
        window.location.href = "/resignGame";
    }
}, 1000);

(function () {
    // not strict to get the Game state object from the HTML response
    //'use strict';

    // This 'this' variable here holds the browser's window object
    // and this is where the 'game.ftl' (FreeMarker) template holds
    // some global Game state data such as the names and piece colors
    // for both players.
    var gameState = this.gameState;

    define(function (require) {
        'use strict';

        // imports
        var GameConstants = require('GameConstants');
        var GameView = require('GameView');

        // create the View object
        var view = new GameView(gameState);

        // launch the Start State (it gets the ball rolling)
        view.setState(GameConstants.START_STATE);
    });

})();
