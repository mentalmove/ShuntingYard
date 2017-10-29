class String
  def tokenize
    tokens = []
    term = self.gsub /\s/, ""
    replacements = { /^\-\(/ => "m1*(", /^\-/ => "m",
       /([+\-\*\/\(])(\-)(\d)/ => '\1m\3', /([+\-\*\/\(])(\-\()/ => '\1m1*(' }
    replacements.each { |pattern, replacement| term.gsub!(pattern, replacement) }
    term.scan(Regexp.union(/m?[\d\.]+/, /[\(\)+\-\*\/]/)).each { |match| tokens << (match.gsub /m/, "-") }
    tokens
  end
  def shunting_yard
    precedence = {
      "+" => 2,
      "-" => 2,
      "*" => 3,
      "/" => 3
    }
    output = []
    operators = []
    tokens = tokenize
    tokens.each do |token|
      if token == "("
        operators << token
      elsif token == ")"
        until operators.empty? || operators.last == "("
          output << operators.pop
        end
        operators.pop
      elsif precedence.has_key? token
        while !operators.empty? && precedence[operators.last] && precedence[operators.last] >= precedence[token]
          output << operators.pop
        end
        operators << token
      else
        output << ((token.to_i == token.to_f) ? token.to_i : token.to_f)
      end
    end
    until operators.empty?
      output << operators.pop
    end
    @output = output
    output
  end
  def calculate
    @output = shunting_yard if !@output
    output = []
    @output.each do |item|
      if item.is_a? Numeric
        output << item
      else
        if output.length > 1
          result = 0
          if item == "+"
            result = output[-2] + output[-1]
          elsif item == "-"
            result = output[-2] - output[-1]
          elsif item == "*"
            result = output[-2] * output[-1]
          elsif output[-1] != 0
            result = output[-2] / output[-1]
          end
          output[-2] = result
          output.pop
        end
      end
    end
    result = output.empty? ? 0 : output[0]
    result = result.to_i if result.to_i == result.to_f
    result
  end
end


puts ""

s = ""
ARGV.each do |argument|
  s += argument
end
if s == ""
  s = gets.chomp
end

puts s.shunting_yard.join(" ")
puts "Result: " + s.calculate.to_s

puts ""
