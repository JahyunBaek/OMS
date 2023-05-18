package ko.co._29cm.homework.oms.Util;

import java.util.List;

import ko.co._29cm.homework.oms.Entity.ProductEntity;

public interface FileManager {
    List<ProductEntity> ReadFile(String csvFileName);
}
