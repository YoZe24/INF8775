# Handle the arguments
while getopts a:e:pt flag
do
    case "${flag}" in
        a) algorithme=${OPTARG};;
        e) ex=${OPTARG};;
        p) opt1='p';;
        t) opt2='t';;
    esac
done

# Choosing the algorithm
case "$algorithme" in
    glouton) path="1";;
    branch_bound) path="2";;
    tabou) path="3";;
esac

# Running the program
java Application $path $ex $opt1 $opt2

# bash tp.sh -a glouton -e ex30_0 -p -t