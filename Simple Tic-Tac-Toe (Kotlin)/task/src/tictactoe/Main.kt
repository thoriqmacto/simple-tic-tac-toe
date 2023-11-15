package tictactoe

import kotlin.math.abs

fun main() {
    // write your code here
    var strInput = "_________"
    printGrid(strInput)

    var promptContinue = true
    var marker = 'X'

    do{
        val prompt = readln()
        val regex = Regex("^[0-9\\s]+\$")
        if (regex.matches(prompt)) {
            val row = prompt.split(" ")[0].toInt()
            val col = prompt.split(" ")[1].toInt()
            if (row in 1..3 && col in 1..3) {
                if (isOccupied(strInput,row,col)){
                    println("This cell is occupied! Choose another one!")
                    promptContinue = true
                }else{
                    val strNew = coordToString(strInput,row,col, marker)
                    printGrid(strNew)

                    val result = analyzeGame(strNew)
                    if (result == "Game not finished"){
                        if (marker == 'X'){
                            marker = 'O'
                        }else if (marker == 'O'){
                            marker = 'X'
                        }
                        strInput = strNew
                        promptContinue = true
                    }else{
                        println(result)
                        promptContinue = false
                    }
                }
            } else {
                println("Coordinates should be from 1 to 3!")
                promptContinue = true
            }
        }else{
            println("You should enter numbers!")
            promptContinue = true
        }
    }while (promptContinue)
}

fun getCharSequence(row: Int,col: Int):Int{
    val seq = when(row.toString() + col.toString()){
        "11" -> 0
        "12" -> 1
        "13" -> 2
        "21" -> 3
        "22" -> 4
        "23" -> 5
        "31" -> 6
        "32" -> 7
        "33" -> 8
        else -> { 99 }
    }
    return seq
}

fun coordToString(strOld:String, row: Int, col: Int, marker: Char = 'X'): String {
    val strSeq = getCharSequence(row,col)
    var strNew = ""
    for (i in strOld.indices){
        if (i == strSeq){
            strNew += marker
        }else{
            strNew += strOld[i]
        }
    }

    return strNew
}

fun isOccupied(strOld:String, row: Int, col: Int): Boolean {
    val chrSeq = getCharSequence(row,col)
    for (i in strOld.indices){
        if (i == chrSeq && strOld[i] != '_'){
            return true
        }
    }
    return false
}

fun analyzeGame(strInput: String): String {
    // count X and O and Empty
    var totalX = 0
    var totalO = 0
    var totalDash = 0
    for(ch in strInput){
        when(ch){
            'X' -> totalX++
            'O' -> totalO++
            '_' -> totalDash++
        }
    }

    val horizontalWin = mutableListOf(
        mutableListOf<Int>(0,1,2),
        mutableListOf<Int>(3,4,5),
        mutableListOf<Int>(6,7,8),
    )

    val verticalWin = mutableListOf(
        mutableListOf<Int>(0,3,6),
        mutableListOf<Int>(1,4,7),
        mutableListOf<Int>(2,5,8),
    )

    val diagonalWin = mutableListOf(
        mutableListOf(0,4,8),
        mutableListOf(2,4,6)
    )

    // analyze winner
    val horiWinX = analyzeWin(strInput,horizontalWin,'X')
    val horiWinO = analyzeWin(strInput,horizontalWin,'O')

    val vertWinX = analyzeWin(strInput,verticalWin, 'X')
    val vertWinO = analyzeWin(strInput,verticalWin,'O')

    val diagWinX = analyzeWin(strInput,diagonalWin,'X')
    val diagWinO = analyzeWin(strInput,diagonalWin,'O')

    return if ( (horiWinX.isNotEmpty() && horiWinO.isNotEmpty()) ||
                (vertWinX.isNotEmpty() && vertWinO.isNotEmpty())){
        "Impossible"
    }else if (abs(totalX-totalO) < 2){
        if(horiWinX.isNotEmpty() || vertWinX.isNotEmpty() || diagWinX.isNotEmpty()){
            "X wins"
        }else if(horiWinO.isNotEmpty() || vertWinO.isNotEmpty() || diagWinO.isNotEmpty()){
            "O wins"
        }else if (totalDash > 0 && (horiWinX.isEmpty() && vertWinX.isEmpty() && diagWinX.isEmpty() &&
        horiWinO.isEmpty() && vertWinO.isEmpty() && diagWinO.isEmpty())) {
            "Game not finished"
        }else{
            "Draw"
        }
    }else{
        "Impossible"
    }
}

fun analyzeWin(strInput:String,winList:MutableList<MutableList<Int>>, marker:Char):String{
    for (i in 0 until winList.size){
        val c1 = strInput[winList[i][0]]
        val c2 = strInput[winList[i][1]]
        val c3 = strInput[winList[i][2]]
        if (c1 == c2 && c2 == c3 && c1 == marker){
            return "$marker wins"
        }
    }
    return ""
}

fun printGrid(strInput:String) {
    val strLen = strInput.length

    // border above
    repeat(strLen){
        print("-")
    }
    println()

    // print grid
    var i = 1
    var row = ""
    for (ch in strInput){
        if(i == 1 || i == 4 || i == 7){
            row += "|"
        }

        if(i % 3 != 0){
            row += " $ch"
        }else{
            row += " $ch |\n"
        }
        i++
    }
    print(row)

    // border bottom
    repeat(strLen){
        print("-")
    }

    println()
}
