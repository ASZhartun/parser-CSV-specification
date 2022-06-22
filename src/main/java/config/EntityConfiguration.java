package config;

import entities.BlockTable;
import entities.Line;
import entities.PositionBar;
import entities.Structure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@org.springframework.context.annotation.Configuration
public class EntityConfiguration {
    @Bean("steelBar")
    @Scope("prototype")
    public PositionBar getPositionBar() {
        return new PositionBar();
    }

    @Bean("structure")
    @Scope("prototype")
    public Structure getStructure() {
        return new Structure();
    }

    @Bean("line")
    @Scope("prototype")
    public Line getLine() {
        return new Line();
    }

    @Bean("blockTable")
    @Scope("prototype")
    public BlockTable getBlockTable() {
        return new BlockTable();
    }

}
