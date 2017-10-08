<?php
    define("VERBOSE", FALSE);

    function lin ($x) {
        $linear = implode(" ", $x);
        $linear = str_replace("( ", "(", $linear);
        $linear = str_replace(" )", ")", $linear);
        echo $linear;
        echo "\n";
        echo "\n";
    }
    function display_calculation ($a) {
        if ( !VERBOSE )
            return;
        echo str_pad(implode(" ", $a[0]), 28);
        echo implode(" ", array_reverse($a[1]));
        echo "\n";
    }
    
    function make_calculation ($first, $second, $operator) {
        switch ( $operator ) {
            case "+":
                return ($first + $second);
            break;
            case "-":
                return ($first - $second);
            break;
            case "*":
                return ($first * $second);
            break;
            case "/":
                if ( !$second )
                    return 0;
                return ($first / $second);
        }
    }
    function calculate_polish ($src) {
        
        $collection = Array(Array(), Array(), Array("+", "-", "*", "/"));
        
        $index = 0;
        for ( $i = 0; $i < count($src); $i++ ) {
            $collection[$index][] = $src[$i];
            if ( $index )
                continue;
            if ( in_array($src[$i], $collection[2]) )
                $index = 1;
        }
        
        $collection[1] = array_reverse($collection[1]);
        
        display_calculation($collection);
        
        while ( count($collection[0]) > 1 ) {
            $index = count($collection[0]) - 3;
            $collection[0][$index] = make_calculation($collection[0][$index], $collection[0][$index + 1], $collection[0][$index + 2]);
            array_pop($collection[0]);
            array_pop($collection[0]);
            while ( count($collection[1]) ) {
                $collection[0][] = array_pop($collection[1]);
                if ( !is_numeric($collection[0][count($collection[0]) - 1]) )
                    break;
            }
            
            display_calculation($collection);
        }
        
        return $collection[0][0];
    }
    function shunting_yard ($src) {
        
        $collection = Array(Array(), Array(), Array("+" => 2, "-" => 2, "*" => 3, "/" => 3));
        
        for ( $i = 0; $i < count($src); $i++ ) {
            if ( is_numeric($src[$i]) ) {
                $collection[0][] = $src[$i];
                continue;
            }
            if ( $src[$i] == "(" ) {
                $collection[1][] = $src[$i];
                continue;
            }
            if ( $src[$i] == ")" ) {
                while ( $collection[1][count($collection[1]) - 1] != "(" )
                    $collection[0][] = array_pop($collection[1]);
                array_pop($collection[1]);
                continue;
            }
            while ( count($collection[1]) && isset($collection[2][$collection[1][count($collection[1]) - 1]]) && $collection[2][$collection[1][count($collection[1]) - 1]] >= $collection[2][$src[$i]] )
                $collection[0][] = array_pop($collection[1]);
            $collection[1][] = $src[$i];
        }
        
        while ( count($collection[1]) )
            $collection[0][] = array_pop($collection[1]);
        
        return $collection[0];
    }
    
    function validate ($src) {
        
        $counter = Array(0, 0, 0);
        
        for ( $i = 0; $i < count($src); $i++ ) {
            if ( $src[$i] == "(" ) {
                $counter[1]++;
                continue;
            }
            if ( $src[$i] == ")" ) {
                $counter[1]--;
                if ( $counter[1] < 0 )
                    return Array($counter[2]);
                continue;
            }
            if ( is_numeric($src[$i]) ) {
                if ( !$counter[2] )
                    $counter[2] = $src[$i];
                $counter[0]++;
                if ( $counter[0] != 1 )
                    return Array($counter[2]);
                continue;
            }
            $counter[0]--;
            if ( $counter[0] )
                return Array($counter[2]);
        }
        
        return ($counter[0] == 1) ? $src : Array($counter[2]);
    }
    
    function beautify_term ($term) {
        
        $term = str_replace(" ", "", $term);
        
        $expressions = Array(
            "/^\-\(/"                   =>   "m1*(",
            "/^\-/"                     =>   "m",
            "/([+\-\*\/\(])(\-)(\d)/"   =>   "$1m$3",
            "/([+\-\*\/\(\)])/"         =>   " $1 ",
            "/^\s+/"                    =>   "",
            "/\s+$/"                    =>   "",
            "/\s+/"                     =>   " "
        );
        
        foreach ( $expressions as $key => $value )
            $term = preg_replace($key, $value, $term);
        
        $term = str_replace("m", "-", $term);
        
        return $term;
    }
    
    echo "\n";
    
    $term = "";
    for ( $i = 1; $i < count($argv); $i++ )
        $term .= $argv[$i];
    if ( !$term )
        $term = readline();
    else
        echo $term . "\n";
    
    echo "\n";
    
    $term = beautify_term($term);
    
    $tokens = explode(" ", $term);
    $tokens = validate($tokens);
    
    lin( $tokens );
    
    $polish = shunting_yard($tokens);
    
    lin( $polish );
    
    $result = calculate_polish($polish);
    
    echo "\n";
    
    echo "Result: " . $result;
    
    echo "\n\n";
?>
