module shunting/yard

go 1.24.3

replace shunting/tokenise => ../tokenise

require (
	shunting/tokenise v0.0.0-00010101000000-000000000000
	yard/shunting v0.0.0-00010101000000-000000000000
)

replace yard/shunting => ../shunting
