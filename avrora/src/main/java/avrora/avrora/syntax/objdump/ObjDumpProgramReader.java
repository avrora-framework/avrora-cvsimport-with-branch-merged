/**
 * Copyright (c) 2004-2005, Regents of the University of California
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name of the University of California, Los Angeles nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package avrora.avrora.syntax.objdump;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import avrora.avrora.arch.legacy.LegacyArchitecture;
import avrora.avrora.core.Program;
import avrora.avrora.core.ProgramReader;
import avrora.avrora.syntax.RawModule;
import avrora.cck.util.Option;
import avrora.cck.util.Util;

/**
 * The <code>ObjdumpProgramReader</code> is an implementation of the
 * <code>ProgramReader</code> that reads source assembly files in the Atmel
 * style syntax. It can handle only one file at a time.
 *
 * @author Ben L. Titzer
 */
public class ObjDumpProgramReader extends ProgramReader
{

    protected final Option.List SECTIONS = newOptionList("sections",
            ".text,.data,.bss",
            "This option specifies a list of sections that the loader should load from "
                    + "the output.");


    /**
     * The <code>read()</code> method takes the command line arguments passed to
     * main and interprets it as a list of filenames to load. It expects only
     * one filename to be present. It will load, parse, and simplify the program
     * and return it.
     *
     * @param args
     *            the string arguments representing the names of the files to
     *            read
     * @return a program obtained by parsing and building the file
     * @throws ParseException
     *             if the file does not parse correctly
     * @throws IOException
     *             if there is a problem reading from the files
     */
    @Override
    public Program read(String[] args) throws Exception
    {
        if (args.length == 0)
            Util.userError("no input files");
        if (args.length != 1)
            Util.userError(
                    "input type \"objdump\" accepts only one file at a time.");
        if (getArchitecture() != LegacyArchitecture.INSTANCE)
            Util.userError(
                    "input type  \"objdump\" parses only the \"legacy\" architecture.");

        File f = new File(args[0]);
        RawModule module = new RawModule(true, true);
        StringBuffer buf = new ObjDumpReformatter(SECTIONS.get())
                .cleanCode(args[0]);
        Reader r = new StringReader(buf.toString());

        // Status.begin("Parsing");
        ObjDumpParser parser = new ObjDumpParser(r, module, f.getName());
        parser.Module();
        // Status.success();
        // Status.begin("Building");
        Program p = module.build();
        // Status.success();
        addIndirectEdges(p);
        return p;
    }


    public ObjDumpProgramReader()
    {
        super("The \"objdump\" input format reads programs that are the "
                + "output of the \"avr-objdump\" utility provided with avr-binutils. "
                + "For example, an ELF file must first be disassembled with "
                + "\"avr-objdump -zhD\" to create a text file readable by this input "
                + "format. The \"-zhD\" options are very important: the output will "
                + "not be parseable otherwise.\n"
                + "The use of this format is deprecated - use ELF instead!");
    }

}
