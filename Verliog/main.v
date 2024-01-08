
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
	output reg[17:0] out
);

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


module EighteenBitComparator(
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
	input clock,
	input [17:0] addr,
	input inc,
	input reset,
	input w_enable,
	output reg [17:0] out
);
	always @(posedge clock) begin
		if (reset == 1) begin
			out <= 18'h0000;
		end
		else if (w_enable == 1) begin
			out <= addr;
		end
		else if (inc == 1) begin
			out <= out + 18'h0001;
		end
		
	end
endmodule


module RegFile(
	input clock,
	input [3:0] w_addr,
	input [17:0] w_data,
	input w_enable,
	input reset,
	input [3:0] r1_addr,
	input [3:0] r2_addr,
	output reg [17:0] r1_out,
	output reg [17:0] r2_out
);
	reg [17:0] regx [0:15];
	always @(posedge clock) begin
		if (reset == 1) begin
			regx[0] <= 18'h0000;
			regx[1] <= 18'h0000;
			regx[2] <= 18'h0000;
			regx[3] <= 18'h0000;
			regx[4] <= 18'h0000;
			regx[5] <= 18'h0000;
			regx[6] <= 18'h0000;
			regx[7] <= 18'h0000;
			regx[8] <= 18'h0000;
			regx[9] <= 18'h0000;
			regx[10] <= 18'h0000;
			regx[11] <= 18'h0000;
			regx[12] <= 18'h0000;
			regx[13] <= 18'h0000;
			regx[14] <= 18'h0000;
			regx[15] <= 18'h0000;
		end
		else if (w_enable == 1) begin
			regx[w_addr] <= w_data;
		end
		r1_out = regx[r1_addr];
		r2_out = regx[r2_addr];
	end

endmodule


module Control_Unit(
	input clock,
	input start,
	input [17:0] instruction,
	input [1:0] flagbits,
	output reg [3:0] alu_select,
	output reg pc_inc, pc_wrt,
	output reg [3:0] r1_addr,
	output reg [3:0] r2_addr,
	output reg [3:0] w_addr,
	output reg regw_enable,
	output reg reset,
	output reg alu_imm,
	output reg ld_signal,
	output reg memwr_signal,
	output reg[9:0] addres_out,
	output reg cmp_signal

);

	reg[4:0] state;
	wire [3:0] opcode = instruction[17:14];
	wire [3:0] first_fb = instruction[13:10];
	wire [3:0] second_fb = instruction[10:7];
	wire [3:0] last_fb = instruction[3:0];
	wire [9:0] addr = instruction[9:0];
	wire [6:0] imm_data = instruction[6:0];
	
	initial
		state = 5'd0;

	always @(posedge clock) begin
		if (state == 5'b00000 && start == 1) begin
			reset = 1;
			state <= 5'b00001;
		end
		else if(state == 5'b00001 && start == 0) begin
			case(opcode)
				4'b0000: begin // ADD
					state <= 5'b00010;
				end
				4'b0001: begin // AND
					state <= 5'b00011;
				end
				4'b0010: begin // NAND
					state <= 5'b00100;
				end
				4'b0011: begin // NOR
					state <= 5'b00101;
				end
				4'b0100: begin // ADDI
					state <= 5'b00110;
				end
				4'b0101: begin // ANDI
					state <= 5'b00111;
				end
				4'b0110: begin // LD
					state <= 5'b01000;
				end
				4'b0111: begin // ST
					state <= 5'b01001;
				end
				4'b1000: begin // CMP
					state <= 5'b01010;
				end
				4'b1001: begin // JUMP
					state <= 5'b01011;
				end
				4'b1010: begin // JE
					state <= 5'b01100;
				end
				4'b1011: begin // JA
					state <= 5'b01101;
				end
				4'b1100: begin // JB
					state <= 5'b01110;
				end
				4'b1101: begin // JAE
					state <= 5'b01111;
				end
				4'b1110: begin // JBE
					state <= 5'b10000;
				end
				
			endcase

		end
		else if (state >= 5'b00001) begin
			case (state)
				4'b0000: begin // ADD
					alu_select = 4'b0001;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0001: begin // AND
					alu_select = 4'b0010;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0010: begin // NAND
					alu_select = 4'b0100;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0011: begin // NOR
					alu_select = 4'b1000;
					r1_addr = second_fb;
					r2_addr = last_fb;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0100: begin // ADDI
					alu_select = 4'b0001;
					r1_addr = second_fb;
					alu_imm = 1;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0101: begin // ANDI
					alu_select = 4'b0010;
					r1_addr = second_fb;
					alu_imm = 1;
					w_addr = first_fb;
					regw_enable = 1;
					pc_inc = 1;
				end
				4'b0110: begin // LD
					ld_signal = 1;
					w_addr = first_fb;
					regw_enable = 1;
					addres_out = addr;
					pc_inc = 1;
				end
				4'b0111: begin // ST
					memwr_signal = 0;
					r1_addr = first_fb;
					addres_out = addr;
					pc_inc = 1;
				end
				4'b1000: begin // CMP
					cmp_signal = 1;
					r1_addr = first_fb;
					r2_addr = last_fb;
					pc_inc = 1;
				end
				4'b1001: begin // JUMP
					pc_inc = 0;
					pc_wrt = 1;
					alu_select = 4'b0001;
					alu_imm = 1;
					
				end
				4'b1010: begin // JE
					if (flagbits == 2'b01) begin
						pc_inc = 0;
						pc_wrt = 1;
						alu_select = 4'b0001;
						alu_imm = 1;
					end
					else begin
						pc_inc = 1;
					end
				end
				4'b1011: begin // JA
					if (flagbits == 2'b00) begin
						pc_inc = 0;
						pc_wrt = 1;
						alu_select = 4'b0001;
						alu_imm = 1;
					end
					else begin
						pc_inc = 1;
					end
				end
				4'b1100: begin // JB
					if (flagbits == 2'b10) begin
						pc_inc = 0;
						pc_wrt = 1;
						alu_select = 4'b0001;
						alu_imm = 1;
					end
					else begin
						pc_inc = 1;
					end
				end
				4'b1101: begin // JAE
					if (flagbits == 2'b00 || flagbits == 2'b01) begin
						pc_inc = 0;
						pc_wrt = 1;
						alu_select = 4'b0001;
						alu_imm = 1;
					end
					else begin
						pc_inc = 1;
					end
				end
				4'b1110: begin // JBE
					if (flagbits == 2'b01 || flagbits == 2'b10) begin
						pc_inc = 0;
						pc_wrt = 1;
						alu_select = 4'b0001;
						alu_imm = 1;
					end
					else begin
						pc_inc = 1;
					end			
				end
			endcase
			reset = 0;
			state <= 5'b00001;
		end

	end

endmodule

module ROM(
	input [9:0] addr,
	output reg [17:0] out
);
	reg [17:0] mem [0:1023];
	initial begin
		$readmemh("instructions_hex.mem", mem);
	end
	always @* begin
		out = mem[addr];
	end
endmodule


module RAM(
	input clock,
	input [9:0] addr,
	input [17:0] w_data,
	input w_enable,
	output reg [17:0] r_data
);
	reg [17:0] mem [0:1023];
	initial begin
		$readmemh("data_hex.mem", mem);
	end
	always @(posedge clock) begin
		if (w_enable == 1) begin
			mem[addr] <= w_data;
		end
		r_data = mem[addr];
	end
endmodule


module flagMem(
	input clock,
	input [1:0] data_in,
	input w_enable,
	input reset,
	output reg [1:0] data_out
);
	reg[1:0] flags;
	always @(posedge clock)
	begin		
		if (reset == 1) begin
			flags <= 2'b00;
		end
		else if (w_enable == 1) begin
			flags <= data_in;
		end
		data_out <= flags;

	end
endmodule


module CPU(
	input clock,
	input start,
	output reg [17:0] out
);
	wire [1:0] flags;
	wire [17:0] instruction;
	wire [17:0] alu_out;
	wire [17:0] r1_out;
	wire [17:0] r2_out;
	reg [17:0] w_data;
	wire [17:0] pc_out;
	wire [1:0] cmp_out;
	wire [17:0] imm_data = {{12{instruction[5]}}, instruction[5:0]};
	wire [3:0] alu_select;
	wire pc_inc, pc_wrt, regw_enable, reset, alu_imm, ld_signal, memwr_signal, cmp_signal;
	wire [3:0] r1_addr, r2_addr, w_addr;
	wire [9:0] addres_out;
	wire [1:0] flagbits;
	
	Control_Unit control_unit_inst(
		.clock(clock),
		.start(start),
		.instruction(instruction),
		.flagbits(flagbits),
		.alu_select(alu_select),
		.pc_inc(pc_inc),
		.pc_wrt(pc_wrt),
		.r1_addr(r1_addr),
		.r2_addr(r2_addr),
		.w_addr(w_addr),
		.regw_enable(regw_enable),
		.reset(reset),
		.alu_imm(alu_imm),
		.ld_signal(ld_signal),
		.memwr_signal(memwr_signal),
		.addres_out(addres_out),
		.cmp_signal(cmp_signal)
	);
	
	PC pc_inst(
		.clock(clock),
		.addr(pc_out),
		.inc(pc_inc),
		.reset(reset),
		.w_enable(pc_wrt),
		.out(pc_out)
	);
	
	RegFile regfile_inst(
		.clock(clock),
		.w_addr(w_addr),
		.w_data(w_data),
		.w_enable(regw_enable),
		.reset(reset),
		.r1_addr(r1_addr),
		.r2_addr(r2_addr),
		.r1_out(r1_out),
		.r2_out(r2_out)
	);
	
	ALU alu_inst(
		.a(r1_out),
		.b(alu_imm ? imm_data : r2_out),
		.select(alu_select),
		.out(alu_out)
	);
	
	ROM rom_inst(
		.addr(addres_out),
		.out(instruction)
	);

	RAM ram_inst(
		.clock(clock),
		.addr(addres_out),
		.w_data(w_data),
		.w_enable(memwr_signal),
		.r_data(r2_out)
	);


	EighteenBitComparator cmp_inst(
		.a(r1_out),
		.b(r2_out),
		.out(cmp_out)
	);

	flagMem flag_inst(
		.clock(clock),
		.data_in(flagbits),
		.w_enable(cmp_signal),
		.reset(reset),
		.data_out(flags)
	);

	always @* begin
		if (ld_signal == 1) begin
			w_data = r2_out;
		end
		else if (memwr_signal == 1) begin
			w_data = r1_out;
		end
		else if (cmp_signal == 1) begin
			w_data = cmp_out;
		end
		else begin
			w_data = alu_out;
		end
	end

endmodule


