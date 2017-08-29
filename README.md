# letters-recognizer
Recognize letters :D

# How it works

- Generate boolean Matrix of size 128*128 for each letter
- Take the image to check and scale it to 128*128
- Convert image to grayScale
- Generate boolean matrix to check (Any color higher than 90 is set to false, otherwise true)
- Print out the result of matching it with each letter and the best match at the end :D