#! /bin/sh

if ! command -v cloc &> /dev/null
then
    echo ""
    echo "-------------------------Warning------------------------------"
    echo "cloc(Count Lines of Code) could not be found."
    echo "You can install the software with command 'brew install cloc'."
    echo "--------------------------------------------------------------"
    exit
fi

cloc ../../ --exclude-dir=build,.gradle,.idea,h2