package service;

import entities.BlockTable;
import entities.Line;
import entities.Structure;

import java.util.ArrayList;


public class ParserCSV {
    private ArrayList<Structure> structures = new ArrayList<>();
    private ArrayList<BlockTable> blockTables = new ArrayList<>();

    public ParserCSV() {

    }

    public void parseTable(ArrayList<String> content) {
        BlockTable blockTable = formTable(content);
        ArrayList<BlockTable> structureBlocks = getBlockStructureListFromCommonTable(blockTable);
        loopParseStructureBlocksData(structureBlocks);
    }

    private BlockTable formTable(ArrayList<String> content) {
        content.remove(0);
        content.remove(0);
        final BlockTable blockTable = new BlockTable();
        for (String stringLine :
                content) {
            final String[] split = stringLine.split(";");
            final Line line;
            try {
                line = new Line(split[0], split[1], split[2], split[3], split[4], split[5]);
                blockTable.getLines().add(line);
            } catch (Exception e) {
                System.out.println("pohui");
            }
        }
        return blockTable;
    }

    private ArrayList<BlockTable> getBlockStructureListFromCommonTable(BlockTable blockTable) {
        final ArrayList<BlockTable> blockTables = new ArrayList<>();
        int upBorderIndex = 0;
        int downBorderIndex = 0;
        for (int i = 1; i < blockTable.getLines().size(); i++) {
            final String name = blockTable.getLines().get(i).getName();
            final String spaceUpperThanNameOfNextBlock = blockTable.getLines().get(i - 1).getName();
            if (!name.equals("")
                    && spaceUpperThanNameOfNextBlock.equals("")) {
                downBorderIndex = i - 1;
                final BlockTable structureBlock = new BlockTable();
                structureBlock.getLines().addAll(blockTable.getLines().subList(upBorderIndex, downBorderIndex + 1));
                blockTables.add(structureBlock);
            }
        }
        return blockTables;
    }

    private void loopParseStructureBlocksData(ArrayList<BlockTable> structureBlocksData) {
        for (BlockTable block :
                structureBlocksData) {
            structures.add(parseCurrentStructureBlock(block));
        }
    }

    private Structure parseCurrentStructureBlock(BlockTable block) {
        final Structure structure = new Structure();
//        structure.setTitle(parseBlockTitle(block));
//        structure.setPositions(parseBlockBarsPositions(block));
//        structure.setConcreteVolume(parseBlockConcreteVolume(block));
        return structure;
    }

    public ArrayList<Structure> getStructures() {
        return structures;
    }

    public void setStructures(ArrayList<Structure> structures) {
        this.structures = structures;
    }
}
