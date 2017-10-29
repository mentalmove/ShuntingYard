class String
  def tokenize
    tokens = []
    term = self.gsub /\s/, ""
    replacements = { /\-([\d\.]+\^)/ => '-1*\1', /^\-\(/ => "m1*(", /^\-/ => "m",
       /([+\-\*\/\(])(\-)(\d)/ => '\1m\3', /([+\-\*\/\(])(\-\()/ => '\1m1*(' }
    replacements.each { |pattern, replacement| term.gsub!(pattern, replacement) }
    term.scan(Regexp.union(/m?[\d\.]+/, /[\(\)+\-\*\/\^]/)).each { |match| tokens << (match.gsub /m/, "-") }
    tokens
  end
  def shunting_yard
    def precedence x, y = nil
      collection = {
        "+" => 2,
        "-" => 2,
        "*" => 3,
        "/" => 3,
        "^" => 4
      }
      right_associative = {
        "^" => true
      }
      if !y
        return collection[x]
      end
      if right_associative[x]
        return collection[x] > collection[y]
      end
      collection[x] >= collection[y]
    end
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
      elsif precedence token
        while !operators.empty? && precedence(operators.last) && precedence(operators.last, token)
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
          if item == "/" && output[-1] == 0
            output[-2] = 0
          elsif item == "^"
            output[-2] = output[-2].to_f ** output[-1]
          else
            output[-2] = output[-2].to_f.send item, output[-1]
          end
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

# Exponent example
# s = "(((6 * 2^7 - -2^2^3)^(1 / 2) - 3^(2^2 - 1) + (1.5^2 - 1))^(1 / 2) * (7^2 + 7^0) - 61)^(1 / 2) + 2 * 17"

if s == ""
  s = gets
end

puts s.shunting_yard.join(" ")
puts "Result: " + s.calculate.to_s

puts ""
