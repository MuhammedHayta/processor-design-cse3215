
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


module 18BitComparator(
	input [17:0] a,
	input [17:0] b,
	output reg [2:0] out
);
	always @* begin
		if (a > b) begin
			out = 3'b100;
		end
		else if (a == b) begin
			out = 3'b010;
		end
		else if (a < b)begin
			out = 3'b001;
		end
	end

endmodule


module PC(
	input [17:0] addr,
	input inc,
	input reset,
	input w_enable,
	output reg [17:0] out
);
	always @* begin
		if (reset == 1) begin
			out = 18'h0000;
		end
		else if (w_enable == 1) begin
			out = addr;
		end
		else if (inc == 1) begin
			out = out + 18'h0001;
		end
		
	end
endmodule


module RegFile(
	input [3:0] w_addr,
	input [17:0] w_data,
	input w_enable,
	input reset,
	input [3:0] r1_addr,
	input [3:0] r2_addr,
	output reg [17:0] r1_out,
	output reg [17:0] r2_out
);
	reg [17:0] reg [0:15];
	always @* begin
		if (reset == 1) begin
			reg[0] = 18'h0000;
			reg[1] = 18'h0000;
			reg[2] = 18'h0000;
			reg[3] = 18'h0000;
			reg[4] = 18'h0000;
			reg[5] = 18'h0000;
			reg[6] = 18'h0000;
			reg[7] = 18'h0000;
			reg[8] = 18'h0000;
			reg[9] = 18'h0000;
			reg[10] = 18'h0000;
			reg[11] = 18'h0000;
			reg[12] = 18'h0000;
			reg[13] = 18'h0000;
			reg[14] = 18'h0000;
			reg[15] = 18'h0000;
		end
		else if (w_enable == 1) begin
			reg[w_addr] = w_data;
		end
		r1_out = reg[r1_addr];
		r2_out = reg[r2_addr];
	end

endmodule


module Control_Unit(
	input start,
	input [17:0] instruction,
	output reg [3:0] alu_select,
	output reg pc_inc, pc_wrt,
	output reg [3:0] r1_addr,
	output reg [3:0] r2_addr,
	output reg [3:0] w_addr,
	output reg regw_enable,
	output reg reset
);


	reg[4:0] state;
	wire [3:0] opcode = instruction[17:14];
	wire [3:0] first_fb = instruction[13:10];
	wire [3:0] second_fb = instruction[10:7];
	wire [3:0] last_fb = instruction[3:0];
	wire [9:0] addr = instruction[9:0];
	wire [6:0] imm_data = instruction[6:0];


	always @* begin
		if (state == 5'b00000 && start == 1) begin
			reset = 1;
			state = 5'b00001;
		end
		else if(state == 5'b00001 && start == 0) begin
			case (opcode)
				4'b0000: begin // ADD
					alu_select = 4'b0000;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0001: begin // AND
					alu_select = 4'b0001;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0010: begin // NAND
					alu_select = 4'b0010;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0011: begin // NOR
					alu_select = 4'b0010;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0100: begin // ADDI
					alu_select = 4'00000;
					r1_addr = second_fb;
					//TODO: WE LEFT HERE!!!
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0101: begin // ANDI
					alu_select = 4'b
				end
				4'b0110: begin // LD
					alu_select = 4'b
				end
				4'b0111: begin // ST
					alu_select = 4'b
				end
				4'b1000: begin // CMP
					alu_select = 4'b
				end
				4'b1001: begin // JUMP
					alu_select = 4'b
				end
				4'b1010: begin // JE
					alu_select = 4'b
				end
				4'b1011: begin // JA
					alu_select = 4'b
				end
				4'b1100: begin // JB
					alu_select = 4'b
				end
				4'b1101: begin // JAE
					alu_select = 4'b
				end
				4'b1110: begin // JBE
					alu_select = 4'b
				end
		endcase
		end
	end

endmodule