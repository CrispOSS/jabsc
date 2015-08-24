#!/bin/bash

in_file="$1"

function plot
{
local data_file="$1"
local mode="$2"

gnuplot <<EOF
reset

unset key
unset colorbox
# See https://github.com/aschn/gnuplot-colorbrewer/blob/master/qualitative/Dark2.plt
set style line 1 lc rgb '#1B9E77' # dark teal
set style line 2 lc rgb '#D95F02' # dark orange
set style line 3 lc rgb '#7570B3' # dark lilac
set style line 4 lc rgb '#E7298A' # dark magenta
set style line 5 lc rgb '#66A61E' # dark lime green
set style line 6 lc rgb '#E6AB02' # dark banana
set style line 7 lc rgb '#A6761D' # dark tan
set style line 8 lc rgb '#666666' # dark gray
set palette maxcolors 8
set palette defined ( 0 '#1B9E77',\
                  1 '#D95F02',\
            2 '#7570B3',\
            3 '#E7298A',\
            4 '#66A61E',\
            5 '#E6AB02',\
            6 '#A6761D',\
            7 '#666666' )


set term ${mode}
set key spacing 1.3
set xlabel "Benchmark"
set xtics nomirror rotate by -90 font ",12"
set ylabel "Time (seconds)"
set grid

set title "Benchmarks" font ",12"

set datafile separator ","
set output "/tmp/plot-benchmark.${mode}"
set style data linespoints

nc="`awk -F, 'NR == 1 { print NF; exit }' ${data_file}`"
# set key autotitle rowhead
set key below
# plot for[i=2:nc] "data.dat" using i:xtic(1) title column(i) ls i*2 lw 3
plot "${data_file}" using 3:xtic(1) title column(3) ls 1 lw 3, \
   "" using 4:xtic(1) title column(4) ls 4 lw 3, \
   "" using 5:xtic(1) title column(5) ls 7 lw 3
EOF
}


# Main

# Check GNU Plot
if type -p gnuplot; then
   echo "Using GNU Plot: $(type -p gnuplot)"
else
   echo "GNU Plot is not installed. Cannot plot."
   exit 0;
fi

echo ""
for t in "svg" "pdf"; do
   plot ${in_file} ${t}
   echo "Plot type: ${t}   done."
done
exit 0
