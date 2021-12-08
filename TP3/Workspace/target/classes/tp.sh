# Handle the arguments
while getopts e:p flag
do
    case "${flag}" in
        e) ex=${OPTARG};;
        p) opt='p';;
    esac
done

# Running the program
java Solution $ex $opt