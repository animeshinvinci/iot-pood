val x = """([A-Za-z0-9]){1,20}"""

"aasdfasdgsdg123asdfasdf12321321".matches(x)
"aaaaaaaaaaaaaaaaaaaaaa".matches(x)