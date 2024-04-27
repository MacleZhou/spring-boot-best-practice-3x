package cn.javastack.data.saver.infra.repository.dao.mapper;

import cn.javastack.data.saver.infra.repository.dao.entity.BaseTable;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseMapper<ID, TABLE extends BaseTable<ID>> {
    private Map<ID, TABLE> tableData = new HashMap<>();

    public ID create(TABLE table){
        if(tableData.containsKey(table.getId())){
            throw new DuplicateKeyException(DuplicateKeyException.class.getName() + ", RECORD " + table.getId() + "ALREADY EXIST");
        }
        tableData.put(table.getId(), table);
        return table.getId();
    }

    public int update(TABLE table){
        if(!tableData.containsKey(table.getId())){
            return 0;
        }
        tableData.put(table.getId(), table);
        return 1;
    }

    public int delete(ID id){
        if(!tableData.containsKey(id)){
            return 0;
        }
        tableData.remove(id);
        return 1;
    }

    public int delete(List<ID> ids){
        int iDeleteRecord = 0;
        for (ID id : ids) {
            iDeleteRecord += delete(id);
        }
        return iDeleteRecord;
    }

    public TABLE getById(String id){
        return tableData.get(id);
    }

    public Map<ID, TABLE> getTableData(){
        return this.tableData;
    }
}
