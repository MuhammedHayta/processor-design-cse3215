module EighteenBitAdder(
	input [17:0] a,
	input [17:0] b,
	output [17:0] out
);

	assign out = a + b;
	
endmodule

module EighteenBitAnd(
	input [17:0] a,
	input [17:0] b,
	output [17:0] out

);
	
	assign out = a & b;

endmodule

module EighteenBitNAnd(
	input [17:0] a,
	input [17:0] b,
	output [17:0] out
);
	
	 
	assign out = ~(a & b);

endmodule

module EighteenBitNOr(
	input [17:0] a,
	input [17:0] b,
	output [17:0] out
);
	
	 
	assign out = ~(a | b);

endmodule

module ALU(
	input [17:0] a,
	input [17:0] b,
	input [3:0] select,
	output [17:0] out
)

  wire [17:0] adder_out, and_out, nand_out, nor_out;

  EighteenBitAdder adder_inst(
    .a(a),
    .b(b),
    .out(adder_out)
  );
	
  EighteenBitAnd and_inst(
    .a(a),
    .b(b),
    .out(and_out)
  );

  EighteenBitNAnd nand_inst(
    .a(a),
    .b(b),
    .out(nand_out)
  );

  EighteenBitNOr nor_inst(
    .a(a),
    .b(b),
    .out(nor_out)
  );

  always @* begin

	case (select)
		4'b0001: out = adder_out; // Addition
		4'b0010: out = and_out; // Bitwise AND
		4'b0100: out = nand_out; // Bitwise NAND
		4'b1000: out = nor_out; // Bitwise NOR
		default: out = 18'h0000; // Default result
	endcase
  end

endmodule
