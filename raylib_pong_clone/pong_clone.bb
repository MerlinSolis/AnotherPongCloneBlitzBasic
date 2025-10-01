AppTitle "Pong Clone Ported From RayLib"
Const screen_width = 1280 : Const screen_height = 800
Graphics screen_width,screen_height,0,2
SetBuffer BackBuffer()
Const fps_cap = 60
fps_timer = CreateTimer(fps_cap)
SeedRnd MilliSecs()

Global font_80
font_80 = LoadFont("Kenney Future.ttf",80)
;font_80 = LoadFont("None",80)

Dim white(2)
white(0) = 255 : white(1) = 255 : white(2) = 255

Dim green(2)
green(0) = 38 : green(1) = 185 : green(2) = 154

Dim dark_green(2)
dark_green(0) = 20 : dark_green(1) = 160 : dark_green(2) = 133

Dim light_green(2)
light_green(0) = 129 : light_green(1) = 204 : light_green(2) = 184

Dim yellow(2)
yellow(0) = 243 : yellow(1) = 213 : yellow(2) = 91


Const UP_KEY = 200 : Const DOWN_KEY = 208

Type Ball
	Field x#
	Field y#
	Field direction#[1]
	Field width#
	Field height#
	Field speed#
	Field rand_dir_choice#[1]
End Type
Function BallInit.Ball(x#,y#,width#,height#)
	ball.Ball = New Ball
	ball\x# = x#
	ball\y# = y#
	ball\width# = width#
	ball\height# = height#
	ball\speed# = 420
	ball\direction#[0] = 1
	ball\direction#[1] = 1
	ball\rand_dir_choice#[0] = -1
	ball\rand_dir_choice#[1] = 1
	Return ball.Ball
End Function
Function BallUpdate(ball.Ball,delta_time#)
	
	ball\x# = ball\x# + ball\direction#[0] * ball\speed# * delta_time#
	ball\y# = ball\y# + ball\direction#[1] * ball\speed# * delta_time#
	
	If ball\y# + ball\height# >= GraphicsHeight() Then
		ball\direction#[1] = ball\direction#[1] * -1
	EndIf
	
	If ball\y# <= 0 Then
		ball\direction#[1] = ball\direction#[1] * -1
	EndIf
	
End Function
Function BallDraw(ball.Ball)
	Color yellow(0),yellow(1),yellow(2)
	Oval ball\x#,ball\y#,ball\width#,ball\height#,True
End Function
Function BallReset(ball.Ball)
	ball\x# = GraphicsWidth()/2 - ball\width#/2
	ball\y# = GraphicsHeight()/2 - ball\height#/2
	ball\direction#[0] = ball\rand_dir_choice#[Rand(0,1)]
	ball\direction#[1] = ball\rand_dir_choice#[Rand(0,1)]
End Function



Type CpuPaddle
	Field x#
	Field y#
	Field direction#[1]
	Field width#
	Field height#
	Field speed#
End Type
Function CpuPaddleInit.CpuPaddle(x#,y#,width#,height#)
	cpu.CpuPaddle = New CpuPaddle
	cpu\x# = x#
	cpu\y# = y#
	cpu\direction#[0] = 0
	cpu\direction#[1] = 0
	cpu\width# = width#
	cpu\height# = height#
	cpu\speed# = 360
	Return cpu.CpuPaddle
End Function
Function CpuPaddleUpdate(cpu.CpuPaddle,ball.Ball,delta_time#)
	
	If cpu\y# + cpu\height#/2 >= ball\y# Then
		cpu\direction#[1] = -1
	EndIf
	
	If cpu\y# + cpu\height#/2 < ball\y# Then
		cpu\direction#[1] = 1
	EndIf
	
	cpu\y# = cpu\y# + cpu\direction#[1] * cpu\speed# * delta_time#
	
	If cpu\y# + cpu\height# >= GraphicsHeight() Then
		cpu\y# = GraphicsHeight() - cpu\height#
	EndIf
	
	If cpu\y# <= 0 Then
		cpu\y# = 0
	EndIf
	
End Function
Function CpuPaddleDraw(cpu.CpuPaddle)
	Color white(0),white(1),white(2)
	Rect cpu\x#,cpu\y#,cpu\width#,cpu\height#,True
End Function




Type PlayerPaddle
	Field x#
	Field y#
	Field direction#[1]
	Field width#
	Field height#
	Field speed#
End Type
Function PlayerPaddleInit.PlayerPaddle(x#,y#,width#,height#)
	player.PlayerPaddle = New PlayerPaddle
	player\x# = x#
	player\y# = y#
	player\direction#[0] = 0
	player\direction#[1] = 0
	player\width# = width#
	player\height# = height#
	player\speed# = 360
	Return player.PlayerPaddle
End Function
Function PlayerPaddleUpdate(player.PlayerPaddle,delta_time#)
	player\direction#[1] = KeyDown(DOWN_KEY) - KeyDown(UP_KEY)
	
	
	player\y# = player\y# + player\direction#[1] * player\speed# * delta_time#
	
	If player\y# + player\height# >= GraphicsHeight() Then
		player\y# = GraphicsHeight() - player\height#
	EndIf
	
	If player\y# <= 0 Then
		player\y# = 0
	EndIf
	
End Function
Function PlayerPaddleDraw(player.PlayerPaddle)
	Color white(0),white(1),white(2)
	Rect player\x#,player\y#,player\width#,player\height#,True
End Function




player.PlayerPaddle = PlayerPaddleInit.PlayerPaddle(GraphicsWidth() - 25 - 10,GraphicsHeight()/2 - 60,25,120)
cpu.CpuPaddle = CpuPaddleInit.CpuPaddle(10,GraphicsHeight()/2 - 60,25,120)
ball.Ball = BallInit.Ball(GraphicsWidth()/2 - 20,GraphicsHeight()/2 - 20,40,40)


Global player_score
player_score = 0
Global cpu_score
cpu_score = 0

frame_counter = 0
delta_time# = 0
current_frames# = 0
last_frame_time = MilliSecs()

ClsColor dark_green(0),dark_green(1),dark_green(2)
Const ESC_KEY = 1
run = True
While run
	If MilliSecs() >= last_frame_time + 1000 Then
		current_frames# = frame_counter
		frame_counter = 0
		delta_time# = 1.0/current_frames#
		last_frame_time = MilliSecs()
	EndIf
	
	
	If KeyHit(ESC_KEY) Then
		run = False
	EndIf
	Cls
	Draw_BG_Elements()
	WaitTimer(fps_timer)
	PlayerPaddleUpdate(player,delta_time#)
	BallUpdate(ball,delta_time#)
	CpuPaddleUpdate(cpu,ball,delta_time#)
	PaddleBallCollisionCheck(player,cpu,ball)
	
	
	PlayerPaddleDraw(player)
	CpuPaddleDraw(cpu)
	BallDraw(ball)
	
	
	PlayerCpuScoreCheck(ball)
	ShowScoreBoard()
	
	
	Flip
	frame_counter = frame_counter + 1
Wend
FlushKeys
WaitKey
End

Function Draw_BG_Elements()
	Color green(0),green(1),green(2)
	Rect GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight(),True
	
	Color light_green(0),light_green(1),light_green(2)
	Oval GraphicsWidth()/2 - 150,GraphicsHeight()/2 - 150,300,300,True
	
	Color white(0),white(1),white(2)
	Line GraphicsWidth()/2 - 1,0,GraphicsWidth()/2 - 1,GraphicsHeight()
	Line GraphicsWidth()/2,0,GraphicsWidth()/2,GraphicsHeight()
	Line GraphicsWidth()/2 + 1,0,GraphicsWidth()/2 + 1,GraphicsHeight()
End Function

Function PaddleBallCollisionCheck(player.PlayerPaddle,cpu.CpuPaddle,ball.Ball)
	If RectsOverlap(ball\x#,ball\y#,ball\width#,ball\height#,player\x#,player\y#,player\width#,player\height#) Then
		ball\direction#[0] = ball\direction#[0] * -1
	EndIf
	
	If RectsOverlap(ball\x#,ball\y#,ball\width#,ball\height#,cpu\x#,cpu\y#,cpu\width#,cpu\height#) Then
		ball\direction#[0] = ball\direction#[0] * -1
	EndIf
End Function

Function PlayerCpuScoreCheck(ball.Ball)
	If ball\x# + ball\width# >= GraphicsWidth() Then
		cpu_score = cpu_score + 1
		BallReset(ball)
	EndIf
	
	If ball\x# <= 0 Then
		player_score = player_score + 1
		BallReset(ball)
	EndIf
End Function

Function ShowScoreBoard()
	SetFont font_80
	Color white(0),white(1),white(2)
	Text GraphicsWidth() * 3/4 + 20,20,Str(player_score)
	Text GraphicsWidth()/4 - 20,20,Str(cpu_score)
	
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D