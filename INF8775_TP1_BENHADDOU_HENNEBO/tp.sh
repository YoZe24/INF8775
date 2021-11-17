# Handle the arguments
while getopts a:e1:e2:pt flag
do
    case "${flag}" in
        a) algorithm=${OPTARG};;
        1) ex1=${OPTARG};;
        2) ex2=${OPTARG};;
        p) opt1='p';;
        t) opt2='t';;
    esac
done

# Choosing the algorithm
case "$algorithm" in
    conv) path="1";;
    strassen) path="2";;
    strassenSeuil) path="3";;
esac

# Compile the program
g++ matrix.cpp -o matrix
# Running the program
./matrix $path $ex1 $ex2 $opt1 $opt2

# bash tp.sh -a strassen -e1 ex2_0 -e2 ex2_1 -p -t