package com.farmassist.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.farmassist.data.local.dao.FarmDao;
import com.farmassist.data.local.dao.FarmDao_Impl;
import com.farmassist.data.local.dao.NewsDao;
import com.farmassist.data.local.dao.NewsDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FarmDatabase_Impl extends FarmDatabase {
  private volatile FarmDao _farmDao;

  private volatile NewsDao _newsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(9) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `district_soil` (`district` TEXT NOT NULL, `soil` TEXT NOT NULL, `defaultTemp` INTEGER NOT NULL, `lat` REAL NOT NULL, `lng` REAL NOT NULL, PRIMARY KEY(`district`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `soil` (`soil` TEXT NOT NULL, `water` TEXT NOT NULL, `fertility` TEXT NOT NULL, PRIMARY KEY(`soil`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `crop` (`crop` TEXT NOT NULL, `soil` TEXT NOT NULL, `season` TEXT NOT NULL, `temp_min` INTEGER NOT NULL, `temp_max` INTEGER NOT NULL, `growing_days` INTEGER NOT NULL, `cost_per_acre` INTEGER NOT NULL, `yield_per_acre` INTEGER NOT NULL, PRIMARY KEY(`crop`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `crop_schedule` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `crop` TEXT NOT NULL, `day` INTEGER NOT NULL, `stage` TEXT NOT NULL, `activity` TEXT NOT NULL, `description` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `fertilizer` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `crop` TEXT NOT NULL, `day` INTEGER NOT NULL, `fertilizer` TEXT NOT NULL, `description` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `irrigation` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `crop` TEXT NOT NULL, `interval_days` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `pest` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `crop` TEXT NOT NULL, `condition` TEXT NOT NULL, `risk` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `waste` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `waste` TEXT NOT NULL, `reuse` TEXT NOT NULL, `steps` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `terrace_farming` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `crop` TEXT NOT NULL, `sunlight` TEXT NOT NULL, `water` TEXT NOT NULL, `days` INTEGER NOT NULL, `difficulty` TEXT NOT NULL, `containerSize` TEXT NOT NULL, `emoji` TEXT NOT NULL, `description` TEXT NOT NULL, `tips` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scheme` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `benefit` TEXT NOT NULL, `eligibility` TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `news` (`guid` TEXT NOT NULL, `title` TEXT NOT NULL, `pubDate` TEXT NOT NULL, `link` TEXT NOT NULL, `description` TEXT NOT NULL, `tag` TEXT NOT NULL, PRIMARY KEY(`guid`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'd5ee5eabade28207aa36052d0aa8ac39')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `district_soil`");
        db.execSQL("DROP TABLE IF EXISTS `soil`");
        db.execSQL("DROP TABLE IF EXISTS `crop`");
        db.execSQL("DROP TABLE IF EXISTS `crop_schedule`");
        db.execSQL("DROP TABLE IF EXISTS `fertilizer`");
        db.execSQL("DROP TABLE IF EXISTS `irrigation`");
        db.execSQL("DROP TABLE IF EXISTS `pest`");
        db.execSQL("DROP TABLE IF EXISTS `waste`");
        db.execSQL("DROP TABLE IF EXISTS `terrace_farming`");
        db.execSQL("DROP TABLE IF EXISTS `scheme`");
        db.execSQL("DROP TABLE IF EXISTS `news`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsDistrictSoil = new HashMap<String, TableInfo.Column>(5);
        _columnsDistrictSoil.put("district", new TableInfo.Column("district", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDistrictSoil.put("soil", new TableInfo.Column("soil", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDistrictSoil.put("defaultTemp", new TableInfo.Column("defaultTemp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDistrictSoil.put("lat", new TableInfo.Column("lat", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDistrictSoil.put("lng", new TableInfo.Column("lng", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDistrictSoil = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDistrictSoil = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDistrictSoil = new TableInfo("district_soil", _columnsDistrictSoil, _foreignKeysDistrictSoil, _indicesDistrictSoil);
        final TableInfo _existingDistrictSoil = TableInfo.read(db, "district_soil");
        if (!_infoDistrictSoil.equals(_existingDistrictSoil)) {
          return new RoomOpenHelper.ValidationResult(false, "district_soil(com.farmassist.data.local.model.DistrictSoil).\n"
                  + " Expected:\n" + _infoDistrictSoil + "\n"
                  + " Found:\n" + _existingDistrictSoil);
        }
        final HashMap<String, TableInfo.Column> _columnsSoil = new HashMap<String, TableInfo.Column>(3);
        _columnsSoil.put("soil", new TableInfo.Column("soil", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoil.put("water", new TableInfo.Column("water", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSoil.put("fertility", new TableInfo.Column("fertility", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSoil = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSoil = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSoil = new TableInfo("soil", _columnsSoil, _foreignKeysSoil, _indicesSoil);
        final TableInfo _existingSoil = TableInfo.read(db, "soil");
        if (!_infoSoil.equals(_existingSoil)) {
          return new RoomOpenHelper.ValidationResult(false, "soil(com.farmassist.data.local.model.Soil).\n"
                  + " Expected:\n" + _infoSoil + "\n"
                  + " Found:\n" + _existingSoil);
        }
        final HashMap<String, TableInfo.Column> _columnsCrop = new HashMap<String, TableInfo.Column>(8);
        _columnsCrop.put("crop", new TableInfo.Column("crop", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("soil", new TableInfo.Column("soil", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("season", new TableInfo.Column("season", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("temp_min", new TableInfo.Column("temp_min", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("temp_max", new TableInfo.Column("temp_max", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("growing_days", new TableInfo.Column("growing_days", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("cost_per_acre", new TableInfo.Column("cost_per_acre", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCrop.put("yield_per_acre", new TableInfo.Column("yield_per_acre", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCrop = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCrop = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCrop = new TableInfo("crop", _columnsCrop, _foreignKeysCrop, _indicesCrop);
        final TableInfo _existingCrop = TableInfo.read(db, "crop");
        if (!_infoCrop.equals(_existingCrop)) {
          return new RoomOpenHelper.ValidationResult(false, "crop(com.farmassist.data.local.model.Crop).\n"
                  + " Expected:\n" + _infoCrop + "\n"
                  + " Found:\n" + _existingCrop);
        }
        final HashMap<String, TableInfo.Column> _columnsCropSchedule = new HashMap<String, TableInfo.Column>(6);
        _columnsCropSchedule.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCropSchedule.put("crop", new TableInfo.Column("crop", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCropSchedule.put("day", new TableInfo.Column("day", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCropSchedule.put("stage", new TableInfo.Column("stage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCropSchedule.put("activity", new TableInfo.Column("activity", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCropSchedule.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCropSchedule = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCropSchedule = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCropSchedule = new TableInfo("crop_schedule", _columnsCropSchedule, _foreignKeysCropSchedule, _indicesCropSchedule);
        final TableInfo _existingCropSchedule = TableInfo.read(db, "crop_schedule");
        if (!_infoCropSchedule.equals(_existingCropSchedule)) {
          return new RoomOpenHelper.ValidationResult(false, "crop_schedule(com.farmassist.data.local.model.CropSchedule).\n"
                  + " Expected:\n" + _infoCropSchedule + "\n"
                  + " Found:\n" + _existingCropSchedule);
        }
        final HashMap<String, TableInfo.Column> _columnsFertilizer = new HashMap<String, TableInfo.Column>(5);
        _columnsFertilizer.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFertilizer.put("crop", new TableInfo.Column("crop", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFertilizer.put("day", new TableInfo.Column("day", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFertilizer.put("fertilizer", new TableInfo.Column("fertilizer", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsFertilizer.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysFertilizer = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesFertilizer = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoFertilizer = new TableInfo("fertilizer", _columnsFertilizer, _foreignKeysFertilizer, _indicesFertilizer);
        final TableInfo _existingFertilizer = TableInfo.read(db, "fertilizer");
        if (!_infoFertilizer.equals(_existingFertilizer)) {
          return new RoomOpenHelper.ValidationResult(false, "fertilizer(com.farmassist.data.local.model.Fertilizer).\n"
                  + " Expected:\n" + _infoFertilizer + "\n"
                  + " Found:\n" + _existingFertilizer);
        }
        final HashMap<String, TableInfo.Column> _columnsIrrigation = new HashMap<String, TableInfo.Column>(3);
        _columnsIrrigation.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIrrigation.put("crop", new TableInfo.Column("crop", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsIrrigation.put("interval_days", new TableInfo.Column("interval_days", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysIrrigation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesIrrigation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoIrrigation = new TableInfo("irrigation", _columnsIrrigation, _foreignKeysIrrigation, _indicesIrrigation);
        final TableInfo _existingIrrigation = TableInfo.read(db, "irrigation");
        if (!_infoIrrigation.equals(_existingIrrigation)) {
          return new RoomOpenHelper.ValidationResult(false, "irrigation(com.farmassist.data.local.model.Irrigation).\n"
                  + " Expected:\n" + _infoIrrigation + "\n"
                  + " Found:\n" + _existingIrrigation);
        }
        final HashMap<String, TableInfo.Column> _columnsPest = new HashMap<String, TableInfo.Column>(4);
        _columnsPest.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPest.put("crop", new TableInfo.Column("crop", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPest.put("condition", new TableInfo.Column("condition", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPest.put("risk", new TableInfo.Column("risk", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPest = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPest = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPest = new TableInfo("pest", _columnsPest, _foreignKeysPest, _indicesPest);
        final TableInfo _existingPest = TableInfo.read(db, "pest");
        if (!_infoPest.equals(_existingPest)) {
          return new RoomOpenHelper.ValidationResult(false, "pest(com.farmassist.data.local.model.Pest).\n"
                  + " Expected:\n" + _infoPest + "\n"
                  + " Found:\n" + _existingPest);
        }
        final HashMap<String, TableInfo.Column> _columnsWaste = new HashMap<String, TableInfo.Column>(4);
        _columnsWaste.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaste.put("waste", new TableInfo.Column("waste", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaste.put("reuse", new TableInfo.Column("reuse", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWaste.put("steps", new TableInfo.Column("steps", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWaste = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWaste = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWaste = new TableInfo("waste", _columnsWaste, _foreignKeysWaste, _indicesWaste);
        final TableInfo _existingWaste = TableInfo.read(db, "waste");
        if (!_infoWaste.equals(_existingWaste)) {
          return new RoomOpenHelper.ValidationResult(false, "waste(com.farmassist.data.local.model.Waste).\n"
                  + " Expected:\n" + _infoWaste + "\n"
                  + " Found:\n" + _existingWaste);
        }
        final HashMap<String, TableInfo.Column> _columnsTerraceFarming = new HashMap<String, TableInfo.Column>(10);
        _columnsTerraceFarming.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("crop", new TableInfo.Column("crop", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("sunlight", new TableInfo.Column("sunlight", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("water", new TableInfo.Column("water", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("days", new TableInfo.Column("days", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("containerSize", new TableInfo.Column("containerSize", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("emoji", new TableInfo.Column("emoji", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTerraceFarming.put("tips", new TableInfo.Column("tips", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTerraceFarming = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTerraceFarming = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTerraceFarming = new TableInfo("terrace_farming", _columnsTerraceFarming, _foreignKeysTerraceFarming, _indicesTerraceFarming);
        final TableInfo _existingTerraceFarming = TableInfo.read(db, "terrace_farming");
        if (!_infoTerraceFarming.equals(_existingTerraceFarming)) {
          return new RoomOpenHelper.ValidationResult(false, "terrace_farming(com.farmassist.data.local.model.TerraceFarming).\n"
                  + " Expected:\n" + _infoTerraceFarming + "\n"
                  + " Found:\n" + _existingTerraceFarming);
        }
        final HashMap<String, TableInfo.Column> _columnsScheme = new HashMap<String, TableInfo.Column>(4);
        _columnsScheme.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheme.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheme.put("benefit", new TableInfo.Column("benefit", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScheme.put("eligibility", new TableInfo.Column("eligibility", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScheme = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScheme = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScheme = new TableInfo("scheme", _columnsScheme, _foreignKeysScheme, _indicesScheme);
        final TableInfo _existingScheme = TableInfo.read(db, "scheme");
        if (!_infoScheme.equals(_existingScheme)) {
          return new RoomOpenHelper.ValidationResult(false, "scheme(com.farmassist.data.local.model.Scheme).\n"
                  + " Expected:\n" + _infoScheme + "\n"
                  + " Found:\n" + _existingScheme);
        }
        final HashMap<String, TableInfo.Column> _columnsNews = new HashMap<String, TableInfo.Column>(6);
        _columnsNews.put("guid", new TableInfo.Column("guid", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNews.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNews.put("pubDate", new TableInfo.Column("pubDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNews.put("link", new TableInfo.Column("link", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNews.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNews.put("tag", new TableInfo.Column("tag", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNews = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNews = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNews = new TableInfo("news", _columnsNews, _foreignKeysNews, _indicesNews);
        final TableInfo _existingNews = TableInfo.read(db, "news");
        if (!_infoNews.equals(_existingNews)) {
          return new RoomOpenHelper.ValidationResult(false, "news(com.farmassist.data.local.model.NewsEntity).\n"
                  + " Expected:\n" + _infoNews + "\n"
                  + " Found:\n" + _existingNews);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "d5ee5eabade28207aa36052d0aa8ac39", "9ddadfd0dd43a17132ebc0ff66a53824");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "district_soil","soil","crop","crop_schedule","fertilizer","irrigation","pest","waste","terrace_farming","scheme","news");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `district_soil`");
      _db.execSQL("DELETE FROM `soil`");
      _db.execSQL("DELETE FROM `crop`");
      _db.execSQL("DELETE FROM `crop_schedule`");
      _db.execSQL("DELETE FROM `fertilizer`");
      _db.execSQL("DELETE FROM `irrigation`");
      _db.execSQL("DELETE FROM `pest`");
      _db.execSQL("DELETE FROM `waste`");
      _db.execSQL("DELETE FROM `terrace_farming`");
      _db.execSQL("DELETE FROM `scheme`");
      _db.execSQL("DELETE FROM `news`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(FarmDao.class, FarmDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(NewsDao.class, NewsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public FarmDao farmDao() {
    if (_farmDao != null) {
      return _farmDao;
    } else {
      synchronized(this) {
        if(_farmDao == null) {
          _farmDao = new FarmDao_Impl(this);
        }
        return _farmDao;
      }
    }
  }

  @Override
  public NewsDao newsDao() {
    if (_newsDao != null) {
      return _newsDao;
    } else {
      synchronized(this) {
        if(_newsDao == null) {
          _newsDao = new NewsDao_Impl(this);
        }
        return _newsDao;
      }
    }
  }
}
