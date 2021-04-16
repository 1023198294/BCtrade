package org.bctrade.code;

import com.owlike.genson.annotation.JsonProperty;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import java.util.Objects;

@DataType
public final class DataInfo {
    @Property
    private final String dataId;
    @Property
    private final String ownerId;
    @Property
    private final String creatorId;
    @Property
    private final String originalDataId;
    @Property
    private final double value;
    @Property
    private final String createdDate;
    @Property
    private final String scratch;

    public String getDataId() {
        return dataId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getOriginalDataId() {
        return originalDataId;
    }

    public double getValue() {
        return value;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getScratch() {
        return scratch;
    }

    public DataInfo(@JsonProperty("dataId") final String dataId,
                    @JsonProperty("ownerId") final String ownerId,
                    @JsonProperty("creatorId") final String creatorId,
                    @JsonProperty("originalDataId") final String originalDataId,
                    @JsonProperty("value") final double value,
                    @JsonProperty("createdDate") final String createdDate,
                    @JsonProperty("scratch") final String scratch) {
        this.dataId = dataId;
        this.ownerId = ownerId;
        this.creatorId = creatorId;
        this.originalDataId = originalDataId;
        this.value = value;
        this.createdDate = createdDate;
        this.scratch = scratch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataInfo dataInfo = (DataInfo) o;
        return Double.compare(dataInfo.value, value) == 0 && Objects.equals(dataId, dataInfo.dataId) && Objects.equals(ownerId, dataInfo.ownerId) && Objects.equals(creatorId, dataInfo.creatorId) && Objects.equals(originalDataId, dataInfo.originalDataId) && Objects.equals(createdDate, dataInfo.createdDate) && Objects.equals(scratch, dataInfo.scratch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, ownerId, creatorId, originalDataId, value, createdDate, scratch);
    }
}
