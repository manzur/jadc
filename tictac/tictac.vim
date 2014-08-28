" Just simple implementation of Tic-tac-toe game for Vim
" User always starts and uses 'X' or 'x' as its token 
" Computer plays as an 'O'
" Call NewBoard() for creating a new game

if exists("g:loaded_tictactoe")
    finish
endif

let g:loaded_tictactoe = 1

let s:NORESULT = 0
let s:XWON     = 1
let s:OWON     = 2
let s:DRAW     = 3

let s:hStep = 4
let s:vStep = 3
let s:linePos = 0
let s:colPos = 0
let s:finished = 0
let s:previousXCount = 0


set updatetime=500

function! NewBoard()
    let s:linePos = line('.') + 3
    let s:colPos  = col('.')  + 2
    
    let s:finished = 0
    let s:previousXCount = 0

    let header = " ___ ___ ___\n"
    let line1  = "|   |   |   |\n"
    let line2  = "| . | . | . |\n"
    let footer = "|___|___|___|\n"
    
    :put =''.header.line1.line2.footer.line1.line2.footer.line1.line2.footer
    write
    
    call cursor(s:linePos, s:colPos)

endfunction

function! s:ConvertCountToResult(xCount, oCount)
    let result = s:NORESULT

    if a:xCount == 3 && a:oCount == 3
        let result = s:DRAW

    elseif a:xCount == 3
        let result = s:XWON

    elseif a:oCount == 3
        let result = s:OWON 
    endif

    return result

endfunction

function! s:CountRow(board, row)
    let xCount = 0
    let oCount = 0

    for column in range(0, 2)
        let c = a:board[a:row][column]

        if c == 'X'
            let xCount += 1
        elseif c == 'O' 
            let oCount += 1
        endif
    endfor

    return [xCount, oCount]

endfunction

function! s:CountColumn(board, column)
    let xCount = 0
    let oCount = 0

    for row in range(0, 2) 
        let c = a:board[row][a:column]

        if c == 'X'
            let xCount += 1

        elseif c == 'O' 
            let oCount += 1
        endif
    endfor

    return [xCount, oCount]

endfunction

function! s:CountDiagonal(board, diagonal)
    " diagonal #0 - is the main diagonal
    " diagonal #1 - is lateral diagonal

    let xCount = 0
    let oCount = 0

    let x = 0
    if a:diagonal == 0
        let y = 0
    else 
        let y = 2
    endif

    for _ in range(1, 3)
        let c = a:board[x][y]

        if c == 'X'
            let xCount += 1
 
        elseif c == 'O' 
            let oCount += 1
        endif
        
        let x += 1
        
        if a:diagonal == 0
            let y += 1
        else 
            let y -= 1
        endif

    endfor

    return [xCount, oCount]

endfunction


function! s:CheckRow(board, row)
" return is :
" 0 - if not result is yet 
" 1 - if the X won
" 2 - if the O won
" 3 - if draw

    let xCount = 0
    let oCount = 0

    let _ = s:CountRow(a:board, a:row)
    let xCount = _[0]
    let oCount = _[1]

    let result = s:ConvertCountToResult(xCount, oCount)
    return result

endfunction

function! s:CheckRows(board)

    let result = 0

    for row in range(0, 2)

        let r = s:CheckRow(a:board, row)

        if r != s:NORESULT
            let result = result == s:NORESULT || result == r ? r : s:DRAW
        endif

        if result == s:DRAW
            break
        endif

    endfor

    return result

endfunction

function! s:CheckColumn(board, column) 
    let xCount = 0
    let oCount = 0

    let _ = s:CountColumn(a:board, a:column)

    let xCount = _[0]
    let oCount = _[1]

    let result = s:ConvertCountToResult(xCount, oCount)
    return result

endfunction 

function! s:CheckColumns(board)

    let result = 0

    for column in range(0, 2) 
        let r = s:CheckColumn(a:board, column)

        if r != 0
            let result = result == s:NORESULT || result == r ? r : s:DRAW 
        endif

        if result == s:DRAW
            break
        endif

    endfor

    return result 

endfunction

function! s:CheckDiagonals(board)
    let xCount = 0
    let oCount = 0

    let _ = s:CountDiagonal(a:board, 0)
    let xCount = _[0]
    let oCount = _[1]

    let result = s:ConvertCountToResult(xCount, oCount)

    if result != s:NORESULT 
        return result
    endif

    let _ = s:CountDiagonal(a:board, 1)
    let xCount = _[0]
    let oCount = _[1]

    let result = s:ConvertCountToResult(xCount, oCount)
    return result

endfunction

function! s:EndOfGame(board)

    let rowResult  = s:CheckRows(a:board)
    if rowResult != s:NORESULT
        return rowResult
    endif

    let columnResult  = s:CheckColumns(a:board)
    if columnResult != s:NORESULT 
        return columnResult
    endif

    let diagonalResult = s:CheckDiagonals(a:board)
    return diagonalResult

endfunction

function! s:ReadBoard()
    let result = [[0, 0, 0], [0, 0, 0], [0, 0, 0]]

    let curV = s:linePos

    for i in range(0, 2) 
        let curH = s:colPos

        let line = getline(curV)

        for j in range(0, 2) 
            let result[i][j] = line[curH - 1] 
            let curH += s:hStep
        endfor

        let curV += s:vStep

    endfor

    return result

endfunction

function! s:UserMadeMove(board)
    let xCount = 0

    for x in range(0, 2) 

        for y in range(0, 2) 
            if a:board[x][y] == 'X'
                let xCount += 1
            endif
        endfor

    endfor

    if xCount != s:previousXCount
        let s:previousXCount = xCount
        let result = 1
    else 
        let result = 0
    endif

    return result
endfunction

function! s:ConvertToCoordinates(x, y)
    let startX = s:linePos 
    let startY = s:colPos

    let resultX = startX + a:x * s:vStep
    let resultY = startY + a:y * s:hStep

    return [resultX, resultY]

endfunction

function! s:Mark(x, y)
    call cursor(a:x, a:y)
    execute "normal rO"
endfunction

function! s:AlmostFilledRow(board, row)
    let _ = s:CountRow(a:board, a:row)
    
    let xCount = _[0]
    let oCount = _[1]

    if xCount == 2 && oCount == 0
        return -1
    elseif oCount == 2 && xCount == 0
        return 1
    else 
        return 0
    endif

endfunction

function! s:FillRow(board, row)
    let _ = s:CountRow(a:board, a:row)
    let xCount = _[0]
    let oCount = _[1]

    for column in range(0, 2) 
        if a:board[a:row][column] == '.'
            
            let _ = s:ConvertToCoordinates(a:row, column)
            
            let x = _[0]
            let y = _[1]

            call s:Mark(x, y)

            break
        endif
    endfor

endfunction

function! s:AlmostFilledColumn(board, column)
    let _ = s:CountColumn(a:board, a:column)
    
    let xCount = _[0]
    let oCount = _[1]

    if xCount == 2 && oCount == 0
        return -1
    elseif oCount == 2 && xCount == 0
        return 1
    else 
        return 0
    endif

endfunction

function! s:FillColumn(board, column)
    let _ = s:CountRow(a:board, a:column)
    let xCount = _[0]
    let oCount = _[1]

    for row in range(0, 2) 
        if a:board[row][a:column] == '.'
            let _ = s:ConvertToCoordinates(row, a:column)
            
            let x = _[0]
            let y = _[1]

            call s:Mark(x, y)

            break
        endif
    endfor

endfunction


function! s:AlmostFilledDiagonal(board, diagonal)
        
    let _ = s:CountDiagonal(a:board, a:diagonal)

    let xCount = _[0]
    let oCount = _[1]

    if xCount == 2 && oCount == 0
        return -1
    elseif oCount == 2 && xCount == 0
        return 1
    else 
        return 0
    endif

endfunction

function! s:FillDiagonal(board, diagonal)
        
    let x = 0
    if a:diagonal == 0
        let y = 0
    else
        let y = 2

    endif

    for _ in range(1, 3)
        if a:board[x][y] == '.' 
            let r = s:ConvertToCoordinates(x, y)
            
            let x = r[0]
            let y = r[1]

            call s:Mark(x, y)

            break
        endif

        let x += 1
        
        if a:diagonal == 0
            let y += 1
        else 
            let y -= 1
        endif

    endfor

endfunction

function! s:FindFirstEmptyCell(board, cells)
    for cell in a:cells

        let x = cell[0]
        let y = cell[1]

        if a:board[x][y] == '.'
            return cell
        endif

    endfor

    return []

endfunction


function! s:MakeMove(board)
    let x = -1
    let y = -1

    "Row checking 
    let row = -1
    for r in range(0, 2) 
        let result = s:AlmostFilledRow(a:board, r) 

        if result != 0 
            let row = r  
            if result == 1
                break
            endif
        endif
    endfor

    if row != -1
        call s:FillRow(a:board, row)
        return
    endif

    "Column checking 
    let column = -1
    for c in range(0, 2) 
        let result = s:AlmostFilledColumn(a:board, c)
        if result != 0
           let column = c
           if result == 1
              break 
           endif
        endif
    endfor

    if column != -1
        call s:FillColumn(a:board, column)
        return
    endif

    "Diagonal checking
    let diagonal = -1
    for d in range(0, 1) 
        let result = s:AlmostFilledDiagonal(a:board, d)
        if result != 0
            let diagonal = d
            if result == 1
                break
            endif
        endif
    endfor

    if diagonal != -1
        call s:FillDiagonal(a:board, diagonal)
        return
    endif

    if a:board[1][1] == '.'
        let x = 1 
        let y = 1

    else
        if a:board[0][0] == 'X'
            let v = s:FindFirstEmptyCell(a:board, [[2, 2], [0, 2], [2, 0]])
        
        elseif a:board[0][2] == 'X'
            let v = s:FindFirstEmptyCell(a:board, [[2, 0], [0, 0], [2, 2]])

        elseif a:board[2][0] == 'X'
            let v = s:FindFirstEmptyCell(a:board, [[0, 2], [0, 0], [2, 2]])

        elseif a:board[2][2] == 'X'
            let v = s:FindFirstEmptyCell(a:board, [[0, 0], [0, 2], [2, 0]])

        else 
            let v = s:FindFirstEmptyCell(a:board, [[0, 0], [0, 2], [2, 0], [2, 2]])

        endif

        if len(v) == 0
            let v = s:FindFirstEmptyCell(a:board, [[0, 1], [1, 0], [1, 2], [2, 1]])
        endif
       
        let x = v[0]
        let y = v[1]

    endif

    if x != -1
        let a = s:ConvertToCoordinates(x, y)
        let x = a[0]
        let y = a[1]

        call s:Mark(x, y)
    endif

endfunction

function! s:BoardIsFilled(board)

    for x in range(0, 2) 
        for y in range(0, 2)
            if a:board[x][y] == '.'        
                return 0
            endif
        endfor
    endfor

    return 1

endfunction

function! s:CheckResult(board)

    let gameResult = s:EndOfGame(a:board) 

    if gameResult == s:XWON
        echo "X won!"
        let s:finished = 1

    elseif gameResult == s:OWON
        echo "O won!"
        let s:finished = 1

    elseif gameResult == s:DRAW || s:BoardIsFilled(a:board)
        echo "Draw!"
        let s:finished = 1
        let gameResult = s:DRAW
    endif

    return gameResult

endfunction

function! s:CheckBoard()
    let board = s:ReadBoard()

    if !s:UserMadeMove(board) || s:finished
        return
    endif

    let gameResult = s:CheckResult(board)

    if gameResult != 0
        return
    endif

    call s:MakeMove(board)

    let board = s:ReadBoard()

    let gameResult = s:CheckResult(board)
    
endfunction

autocmd cursorhold * call s:CheckBoard()
call NewBoard()
