package dao;

import api.ReceiptResponse;
import generated.tables.records.ReceiptsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;

public class ReceiptDao {
    DSLContext dsl;
    HashMap <String, ArrayList<Integer>> hm = new HashMap<>();

    public ReceiptDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String merchantName, BigDecimal amount) {
        ReceiptsRecord receiptsRecord = dsl
                .insertInto(RECEIPTS, RECEIPTS.MERCHANT, RECEIPTS.AMOUNT)
                .values(merchantName, amount)
                .returning(RECEIPTS.ID)
                .fetchOne();

        checkState(receiptsRecord != null && receiptsRecord.getId() != null, "Insert failed");

        return receiptsRecord.getId();
    }

    public List<ReceiptResponse> getAllReceipts() {
        List<ReceiptsRecord> records = dsl.selectFrom(RECEIPTS).fetch();
        ArrayList<ReceiptResponse> responses = new ArrayList<>();
        for (ReceiptsRecord record : records) {
            ArrayList<String> tags = new ArrayList<>();
            for (String tag : hm.keySet()){
                if(hm.get(tag).contains(record.getId()))
                    tags.add(tag);
            }
            ReceiptResponse response = new ReceiptResponse(record);
            response.setTags(tags);
            responses.add(response);
        }
        return responses;
    }

    public void insert(String tagName, int id){
        if (!hm.containsKey(tagName)) hm.put(tagName, new ArrayList<>());
        if (hm.get(tagName).contains(id)) hm.get(tagName).remove(Integer.valueOf(id));
        else hm.get(tagName).add(id);
    }

    public List<ReceiptsRecord> getReceiptsByTag(String tagName) {
        ArrayList<Integer> ids = hm.get(tagName);
        if (ids == null || ids.size() == 0) return new ArrayList<>();
        List<ReceiptsRecord> res = new ArrayList<>();
        for (ReceiptsRecord receipt : dsl.selectFrom(RECEIPTS).fetch()){
            if (ids.contains(receipt.getId())) res.add(receipt);
        }
        return res;
    }
}
