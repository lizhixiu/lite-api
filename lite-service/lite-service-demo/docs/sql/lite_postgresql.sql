CREATE TABLE lite_api_info (
     id VARCHAR(32) NOT NULL,
     class_id VARCHAR(32) DEFAULT NULL,
     table_code VARCHAR(32) DEFAULT NULL,
     table_name VARCHAR(128) DEFAULT NULL,
     table_desc VARCHAR(1024) DEFAULT NULL,
     rec_en_status VARCHAR(4) DEFAULT NULL,
     rec_del_status VARCHAR(4) DEFAULT NULL,
     rec_sys_status VARCHAR(4) DEFAULT NULL,
     rec_sort DECIMAL(5,0) DEFAULT NULL,
     rec_memo VARCHAR(1024) DEFAULT NULL,
     rec_create_uid VARCHAR(32) DEFAULT NULL,
     rec_create_uname VARCHAR(64) DEFAULT NULL,
     rec_create_datetime TIMESTAMP DEFAULT NULL,
     rec_update_uid VARCHAR(32) DEFAULT NULL,
     rec_update_uname VARCHAR(64) DEFAULT NULL,
     rec_update_datetime TIMESTAMP DEFAULT NULL,
     PRIMARY KEY (id)
);

COMMENT ON TABLE lite_api_info IS '数据表';

COMMENT ON COLUMN lite_api_info.id IS '主键';
COMMENT ON COLUMN lite_api_info.class_id IS '分类id';
COMMENT ON COLUMN lite_api_info.table_code IS '表名';
COMMENT ON COLUMN lite_api_info.table_name IS '表中文名';
COMMENT ON COLUMN lite_api_info.table_desc IS '表描述';
COMMENT ON COLUMN lite_api_info.rec_en_status IS '是否启用';
COMMENT ON COLUMN lite_api_info.rec_del_status IS '是否删除';
COMMENT ON COLUMN lite_api_info.rec_sys_status IS '是否系统保留';
COMMENT ON COLUMN lite_api_info.rec_sort IS '排序号';
COMMENT ON COLUMN lite_api_info.rec_memo IS '备注';
COMMENT ON COLUMN lite_api_info.rec_create_uid IS '创建人ID';
COMMENT ON COLUMN lite_api_info.rec_create_uname IS '创建人';
COMMENT ON COLUMN lite_api_info.rec_create_datetime IS '创建时间';
COMMENT ON COLUMN lite_api_info.rec_update_uid IS '更新人ID';
COMMENT ON COLUMN lite_api_info.rec_update_uname IS '更新人';
COMMENT ON COLUMN lite_api_info.rec_update_datetime IS '更新时间';

INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('09eefc2286284374aeb00071aeb90781', '18a293ddee4f4aee8da80a35fda1e98a', 'IST_TASK', '巡检任务', NULL, '1', '0', '0', 1, NULL, '1', '轩晨', '2025-09-21 01:05:08', '1', '轩晨', '2025-09-21 01:05:20');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('0df90238c0334762bbf650b207d7081b', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_POINT_CLASS', '巡检点分类', NULL, '1', '0', '0', 10, NULL, NULL, NULL, NULL, '1', '轩晨', '2025-09-20 11:10:09');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('11359e43dea649009d58e5f1bb924592', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_ITEM_CLASS', '巡检项分类', NULL, '1', '0', '0', 20, NULL, '1', '轩晨', '2025-09-20 10:59:57', '1', '轩晨', '2025-09-20 11:12:11');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('4bd3cec636fb4e7aba907aa1020bea17', '18a293ddee4f4aee8da80a35fda1e98a', 'IST_TASK_POINT', '巡检任务点', NULL, '1', '0', '0', 2, NULL, '1', '轩晨', '2025-09-21 01:05:08', '1', '轩晨', '2025-09-21 01:05:25');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('645a705c36024cd488146f398176751c', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_ITEM', '巡检项', NULL, '1', '0', '0', 26, NULL, '1', '轩晨', '2025-09-20 10:59:57', '1', '轩晨', '2025-09-20 11:00:30');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('6c3945e2669c4da681257de0bd2aad24', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_LINE_POINT', '巡检线路点', NULL, '1', '0', '0', 40, NULL, '1', '轩晨', '2025-09-20 14:09:22', '1', '轩晨', '2025-09-20 14:09:56');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('75c641c622224641a15ca0002c2715d3', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_POINT', '巡检点', NULL, '1', '0', '0', 15, NULL, NULL, NULL, NULL, '1', '轩晨', '2025-09-20 11:12:26');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('9ae03ba77ba948ac90e79a52d3651352', '18a293ddee4f4aee8da80a35fda1e98a', 'IST_TASK_ITEM_ERROR', '巡检项异常记录', NULL, '1', '0', '0', 5, NULL, '1', '轩晨', '2025-09-21 01:05:08', NULL, NULL, NULL);
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('add4d8ae378b40e9a536e7e8bde618ef', '18a293ddee4f4aee8da80a35fda1e98a', 'IST_TASK_ITEM', '巡检任务项', NULL, '1', '0', '0', 3, NULL, '1', '轩晨', '2025-09-21 01:05:08', '1', '轩晨', '2025-09-21 01:05:35');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('d178a83093a34066845e22417d4942fb', '18a293ddee4f4aee8da80a35fda1e98a', 'IST_TASK_DEVICE', '巡检任务设备', NULL, '1', '0', '0', 4, NULL, '1', '轩晨', '2025-09-21 01:05:08', '1', '轩晨', '2025-09-21 01:05:45');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('d5ed2c33376e490a830d9f8326a3e803', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_LINE', '巡检线路', NULL, '1', '0', '0', 35, NULL, '1', '轩晨', '2025-09-20 14:09:22', '1', '轩晨', '2025-09-20 14:09:50');
INSERT INTO lite_api_info (id, class_id, table_code, table_name, table_desc, rec_en_status, rec_del_status, rec_sys_status, rec_sort, rec_memo, rec_create_uid, rec_create_uname, rec_create_datetime, rec_update_uid, rec_update_uname, rec_update_datetime) VALUES ('e174ef63925b4e76a545a3c2fee4725e', 'c6ce55f631d84141ac5376a0d97cbfc2', 'IST_LINE_CLASS', '巡检线路分类', NULL, '1', '0', '0', 30, NULL, '1', '轩晨', '2025-09-20 14:09:22', '1', '轩晨', '2025-09-20 14:09:45');