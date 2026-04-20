package com.farmassist.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.farmassist.data.local.Converters;
import com.farmassist.data.local.model.Crop;
import com.farmassist.data.local.model.CropSchedule;
import com.farmassist.data.local.model.DistrictSoil;
import com.farmassist.data.local.model.Fertilizer;
import com.farmassist.data.local.model.Irrigation;
import com.farmassist.data.local.model.Pest;
import com.farmassist.data.local.model.Scheme;
import com.farmassist.data.local.model.Soil;
import com.farmassist.data.local.model.TerraceFarming;
import com.farmassist.data.local.model.Waste;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FarmDao_Impl implements FarmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DistrictSoil> __insertionAdapterOfDistrictSoil;

  private final EntityInsertionAdapter<Soil> __insertionAdapterOfSoil;

  private final EntityInsertionAdapter<Crop> __insertionAdapterOfCrop;

  private final Converters __converters = new Converters();

  private final EntityInsertionAdapter<CropSchedule> __insertionAdapterOfCropSchedule;

  private final EntityInsertionAdapter<Fertilizer> __insertionAdapterOfFertilizer;

  private final EntityInsertionAdapter<Irrigation> __insertionAdapterOfIrrigation;

  private final EntityInsertionAdapter<Pest> __insertionAdapterOfPest;

  private final EntityInsertionAdapter<Waste> __insertionAdapterOfWaste;

  private final EntityInsertionAdapter<TerraceFarming> __insertionAdapterOfTerraceFarming;

  private final EntityInsertionAdapter<Scheme> __insertionAdapterOfScheme;

  private final SharedSQLiteStatement __preparedStmtOfClearSchemes;

  public FarmDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDistrictSoil = new EntityInsertionAdapter<DistrictSoil>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `district_soil` (`district`,`soil`,`defaultTemp`,`lat`,`lng`) VALUES (?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final DistrictSoil entity) {
        statement.bindString(1, entity.getDistrict());
        statement.bindString(2, entity.getSoil());
        statement.bindLong(3, entity.getDefaultTemp());
        statement.bindDouble(4, entity.getLat());
        statement.bindDouble(5, entity.getLng());
      }
    };
    this.__insertionAdapterOfSoil = new EntityInsertionAdapter<Soil>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `soil` (`soil`,`water`,`fertility`) VALUES (?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Soil entity) {
        statement.bindString(1, entity.getSoil());
        statement.bindString(2, entity.getWater());
        statement.bindString(3, entity.getFertility());
      }
    };
    this.__insertionAdapterOfCrop = new EntityInsertionAdapter<Crop>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `crop` (`crop`,`soil`,`season`,`temp_min`,`temp_max`,`growing_days`,`cost_per_acre`,`yield_per_acre`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Crop entity) {
        statement.bindString(1, entity.getCrop());
        final String _tmp = __converters.fromStringList(entity.getSoil());
        statement.bindString(2, _tmp);
        statement.bindString(3, entity.getSeason());
        statement.bindLong(4, entity.getTemp_min());
        statement.bindLong(5, entity.getTemp_max());
        statement.bindLong(6, entity.getGrowing_days());
        statement.bindLong(7, entity.getCost_per_acre());
        statement.bindLong(8, entity.getYield_per_acre());
      }
    };
    this.__insertionAdapterOfCropSchedule = new EntityInsertionAdapter<CropSchedule>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `crop_schedule` (`id`,`crop`,`day`,`stage`,`activity`,`description`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CropSchedule entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCrop());
        statement.bindLong(3, entity.getDay());
        statement.bindString(4, entity.getStage());
        statement.bindString(5, entity.getActivity());
        statement.bindString(6, entity.getDescription());
      }
    };
    this.__insertionAdapterOfFertilizer = new EntityInsertionAdapter<Fertilizer>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `fertilizer` (`id`,`crop`,`day`,`fertilizer`,`description`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Fertilizer entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCrop());
        statement.bindLong(3, entity.getDay());
        statement.bindString(4, entity.getFertilizer());
        statement.bindString(5, entity.getDescription());
      }
    };
    this.__insertionAdapterOfIrrigation = new EntityInsertionAdapter<Irrigation>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `irrigation` (`id`,`crop`,`interval_days`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Irrigation entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCrop());
        statement.bindLong(3, entity.getInterval_days());
      }
    };
    this.__insertionAdapterOfPest = new EntityInsertionAdapter<Pest>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `pest` (`id`,`crop`,`condition`,`risk`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Pest entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCrop());
        statement.bindString(3, entity.getCondition());
        statement.bindString(4, entity.getRisk());
      }
    };
    this.__insertionAdapterOfWaste = new EntityInsertionAdapter<Waste>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `waste` (`id`,`waste`,`reuse`,`steps`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Waste entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getWaste());
        statement.bindString(3, entity.getReuse());
        final String _tmp = __converters.fromStringList(entity.getSteps());
        statement.bindString(4, _tmp);
      }
    };
    this.__insertionAdapterOfTerraceFarming = new EntityInsertionAdapter<TerraceFarming>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `terrace_farming` (`id`,`crop`,`sunlight`,`water`,`days`,`difficulty`,`containerSize`,`emoji`,`description`,`tips`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final TerraceFarming entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getCrop());
        statement.bindString(3, entity.getSunlight());
        statement.bindString(4, entity.getWater());
        statement.bindLong(5, entity.getDays());
        statement.bindString(6, entity.getDifficulty());
        statement.bindString(7, entity.getContainerSize());
        statement.bindString(8, entity.getEmoji());
        statement.bindString(9, entity.getDescription());
        final String _tmp = __converters.fromStringList(entity.getTips());
        statement.bindString(10, _tmp);
      }
    };
    this.__insertionAdapterOfScheme = new EntityInsertionAdapter<Scheme>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scheme` (`id`,`name`,`benefit`,`eligibility`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Scheme entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindString(3, entity.getBenefit());
        statement.bindString(4, entity.getEligibility());
      }
    };
    this.__preparedStmtOfClearSchemes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM scheme";
        return _query;
      }
    };
  }

  @Override
  public Object insertDistrictSoils(final List<DistrictSoil> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfDistrictSoil.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSoils(final List<Soil> items, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSoil.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCrops(final List<Crop> items, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCrop.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertCropSchedules(final List<CropSchedule> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCropSchedule.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertFertilizers(final List<Fertilizer> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFertilizer.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertIrrigation(final List<Irrigation> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfIrrigation.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPests(final List<Pest> items, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPest.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertWastes(final List<Waste> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfWaste.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertTerraceFarming(final List<TerraceFarming> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfTerraceFarming.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSchemes(final List<Scheme> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScheme.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object clearSchemes(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfClearSchemes.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfClearSchemes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getDistrictInfo(final String district,
      final Continuation<? super DistrictSoil> $completion) {
    final String _sql = "SELECT * FROM district_soil WHERE district = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, district);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<DistrictSoil>() {
      @Override
      @Nullable
      public DistrictSoil call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfSoil = CursorUtil.getColumnIndexOrThrow(_cursor, "soil");
          final int _cursorIndexOfDefaultTemp = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultTemp");
          final int _cursorIndexOfLat = CursorUtil.getColumnIndexOrThrow(_cursor, "lat");
          final int _cursorIndexOfLng = CursorUtil.getColumnIndexOrThrow(_cursor, "lng");
          final DistrictSoil _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final String _tmpSoil;
            _tmpSoil = _cursor.getString(_cursorIndexOfSoil);
            final int _tmpDefaultTemp;
            _tmpDefaultTemp = _cursor.getInt(_cursorIndexOfDefaultTemp);
            final double _tmpLat;
            _tmpLat = _cursor.getDouble(_cursorIndexOfLat);
            final double _tmpLng;
            _tmpLng = _cursor.getDouble(_cursorIndexOfLng);
            _result = new DistrictSoil(_tmpDistrict,_tmpSoil,_tmpDefaultTemp,_tmpLat,_tmpLng);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<DistrictSoil>> getAllDistricts() {
    final String _sql = "SELECT * FROM district_soil ORDER BY district ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"district_soil"}, new Callable<List<DistrictSoil>>() {
      @Override
      @NonNull
      public List<DistrictSoil> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDistrict = CursorUtil.getColumnIndexOrThrow(_cursor, "district");
          final int _cursorIndexOfSoil = CursorUtil.getColumnIndexOrThrow(_cursor, "soil");
          final int _cursorIndexOfDefaultTemp = CursorUtil.getColumnIndexOrThrow(_cursor, "defaultTemp");
          final int _cursorIndexOfLat = CursorUtil.getColumnIndexOrThrow(_cursor, "lat");
          final int _cursorIndexOfLng = CursorUtil.getColumnIndexOrThrow(_cursor, "lng");
          final List<DistrictSoil> _result = new ArrayList<DistrictSoil>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final DistrictSoil _item;
            final String _tmpDistrict;
            _tmpDistrict = _cursor.getString(_cursorIndexOfDistrict);
            final String _tmpSoil;
            _tmpSoil = _cursor.getString(_cursorIndexOfSoil);
            final int _tmpDefaultTemp;
            _tmpDefaultTemp = _cursor.getInt(_cursorIndexOfDefaultTemp);
            final double _tmpLat;
            _tmpLat = _cursor.getDouble(_cursorIndexOfLat);
            final double _tmpLng;
            _tmpLng = _cursor.getDouble(_cursorIndexOfLng);
            _item = new DistrictSoil(_tmpDistrict,_tmpSoil,_tmpDefaultTemp,_tmpLat,_tmpLng);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getSoilDetails(final String soil, final Continuation<? super Soil> $completion) {
    final String _sql = "SELECT * FROM soil WHERE soil = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, soil);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Soil>() {
      @Override
      @Nullable
      public Soil call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfSoil = CursorUtil.getColumnIndexOrThrow(_cursor, "soil");
          final int _cursorIndexOfWater = CursorUtil.getColumnIndexOrThrow(_cursor, "water");
          final int _cursorIndexOfFertility = CursorUtil.getColumnIndexOrThrow(_cursor, "fertility");
          final Soil _result;
          if (_cursor.moveToFirst()) {
            final String _tmpSoil;
            _tmpSoil = _cursor.getString(_cursorIndexOfSoil);
            final String _tmpWater;
            _tmpWater = _cursor.getString(_cursorIndexOfWater);
            final String _tmpFertility;
            _tmpFertility = _cursor.getString(_cursorIndexOfFertility);
            _result = new Soil(_tmpSoil,_tmpWater,_tmpFertility);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllCrops(final Continuation<? super List<Crop>> $completion) {
    final String _sql = "SELECT * FROM crop";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Crop>>() {
      @Override
      @NonNull
      public List<Crop> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfSoil = CursorUtil.getColumnIndexOrThrow(_cursor, "soil");
          final int _cursorIndexOfSeason = CursorUtil.getColumnIndexOrThrow(_cursor, "season");
          final int _cursorIndexOfTempMin = CursorUtil.getColumnIndexOrThrow(_cursor, "temp_min");
          final int _cursorIndexOfTempMax = CursorUtil.getColumnIndexOrThrow(_cursor, "temp_max");
          final int _cursorIndexOfGrowingDays = CursorUtil.getColumnIndexOrThrow(_cursor, "growing_days");
          final int _cursorIndexOfCostPerAcre = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_per_acre");
          final int _cursorIndexOfYieldPerAcre = CursorUtil.getColumnIndexOrThrow(_cursor, "yield_per_acre");
          final List<Crop> _result = new ArrayList<Crop>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Crop _item;
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final List<String> _tmpSoil;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSoil);
            _tmpSoil = __converters.toStringList(_tmp);
            final String _tmpSeason;
            _tmpSeason = _cursor.getString(_cursorIndexOfSeason);
            final int _tmpTemp_min;
            _tmpTemp_min = _cursor.getInt(_cursorIndexOfTempMin);
            final int _tmpTemp_max;
            _tmpTemp_max = _cursor.getInt(_cursorIndexOfTempMax);
            final int _tmpGrowing_days;
            _tmpGrowing_days = _cursor.getInt(_cursorIndexOfGrowingDays);
            final int _tmpCost_per_acre;
            _tmpCost_per_acre = _cursor.getInt(_cursorIndexOfCostPerAcre);
            final int _tmpYield_per_acre;
            _tmpYield_per_acre = _cursor.getInt(_cursorIndexOfYieldPerAcre);
            _item = new Crop(_tmpCrop,_tmpSoil,_tmpSeason,_tmpTemp_min,_tmpTemp_max,_tmpGrowing_days,_tmpCost_per_acre,_tmpYield_per_acre);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecommendedCrops(final String soilType, final String season, final int temp,
      final Continuation<? super List<Crop>> $completion) {
    final String _sql = "SELECT * FROM crop WHERE temp_min <= ? AND temp_max >= ? AND season IN (?, 'All') AND soil LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 4);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, temp);
    _argIndex = 2;
    _statement.bindLong(_argIndex, temp);
    _argIndex = 3;
    _statement.bindString(_argIndex, season);
    _argIndex = 4;
    _statement.bindString(_argIndex, soilType);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Crop>>() {
      @Override
      @NonNull
      public List<Crop> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfSoil = CursorUtil.getColumnIndexOrThrow(_cursor, "soil");
          final int _cursorIndexOfSeason = CursorUtil.getColumnIndexOrThrow(_cursor, "season");
          final int _cursorIndexOfTempMin = CursorUtil.getColumnIndexOrThrow(_cursor, "temp_min");
          final int _cursorIndexOfTempMax = CursorUtil.getColumnIndexOrThrow(_cursor, "temp_max");
          final int _cursorIndexOfGrowingDays = CursorUtil.getColumnIndexOrThrow(_cursor, "growing_days");
          final int _cursorIndexOfCostPerAcre = CursorUtil.getColumnIndexOrThrow(_cursor, "cost_per_acre");
          final int _cursorIndexOfYieldPerAcre = CursorUtil.getColumnIndexOrThrow(_cursor, "yield_per_acre");
          final List<Crop> _result = new ArrayList<Crop>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Crop _item;
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final List<String> _tmpSoil;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSoil);
            _tmpSoil = __converters.toStringList(_tmp);
            final String _tmpSeason;
            _tmpSeason = _cursor.getString(_cursorIndexOfSeason);
            final int _tmpTemp_min;
            _tmpTemp_min = _cursor.getInt(_cursorIndexOfTempMin);
            final int _tmpTemp_max;
            _tmpTemp_max = _cursor.getInt(_cursorIndexOfTempMax);
            final int _tmpGrowing_days;
            _tmpGrowing_days = _cursor.getInt(_cursorIndexOfGrowingDays);
            final int _tmpCost_per_acre;
            _tmpCost_per_acre = _cursor.getInt(_cursorIndexOfCostPerAcre);
            final int _tmpYield_per_acre;
            _tmpYield_per_acre = _cursor.getInt(_cursorIndexOfYieldPerAcre);
            _item = new Crop(_tmpCrop,_tmpSoil,_tmpSeason,_tmpTemp_min,_tmpTemp_max,_tmpGrowing_days,_tmpCost_per_acre,_tmpYield_per_acre);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getScheduleForCrop(final String crop,
      final Continuation<? super List<CropSchedule>> $completion) {
    final String _sql = "SELECT * FROM crop_schedule WHERE crop = ? ORDER BY day ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, crop);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<CropSchedule>>() {
      @Override
      @NonNull
      public List<CropSchedule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "day");
          final int _cursorIndexOfStage = CursorUtil.getColumnIndexOrThrow(_cursor, "stage");
          final int _cursorIndexOfActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "activity");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<CropSchedule> _result = new ArrayList<CropSchedule>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CropSchedule _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final int _tmpDay;
            _tmpDay = _cursor.getInt(_cursorIndexOfDay);
            final String _tmpStage;
            _tmpStage = _cursor.getString(_cursorIndexOfStage);
            final String _tmpActivity;
            _tmpActivity = _cursor.getString(_cursorIndexOfActivity);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item = new CropSchedule(_tmpId,_tmpCrop,_tmpDay,_tmpStage,_tmpActivity,_tmpDescription);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getFertilizersForCrop(final String crop,
      final Continuation<? super List<Fertilizer>> $completion) {
    final String _sql = "SELECT * FROM fertilizer WHERE crop = ? ORDER BY day ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, crop);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Fertilizer>>() {
      @Override
      @NonNull
      public List<Fertilizer> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfDay = CursorUtil.getColumnIndexOrThrow(_cursor, "day");
          final int _cursorIndexOfFertilizer = CursorUtil.getColumnIndexOrThrow(_cursor, "fertilizer");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final List<Fertilizer> _result = new ArrayList<Fertilizer>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Fertilizer _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final int _tmpDay;
            _tmpDay = _cursor.getInt(_cursorIndexOfDay);
            final String _tmpFertilizer;
            _tmpFertilizer = _cursor.getString(_cursorIndexOfFertilizer);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            _item = new Fertilizer(_tmpId,_tmpCrop,_tmpDay,_tmpFertilizer,_tmpDescription);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getIrrigationForCrop(final String crop,
      final Continuation<? super Irrigation> $completion) {
    final String _sql = "SELECT * FROM irrigation WHERE crop = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, crop);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Irrigation>() {
      @Override
      @Nullable
      public Irrigation call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfIntervalDays = CursorUtil.getColumnIndexOrThrow(_cursor, "interval_days");
          final Irrigation _result;
          if (_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final int _tmpInterval_days;
            _tmpInterval_days = _cursor.getInt(_cursorIndexOfIntervalDays);
            _result = new Irrigation(_tmpId,_tmpCrop,_tmpInterval_days);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPestsForCrop(final String crop,
      final Continuation<? super List<Pest>> $completion) {
    final String _sql = "SELECT * FROM pest WHERE crop = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, crop);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Pest>>() {
      @Override
      @NonNull
      public List<Pest> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfCondition = CursorUtil.getColumnIndexOrThrow(_cursor, "condition");
          final int _cursorIndexOfRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "risk");
          final List<Pest> _result = new ArrayList<Pest>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Pest _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final String _tmpCondition;
            _tmpCondition = _cursor.getString(_cursorIndexOfCondition);
            final String _tmpRisk;
            _tmpRisk = _cursor.getString(_cursorIndexOfRisk);
            _item = new Pest(_tmpId,_tmpCrop,_tmpCondition,_tmpRisk);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllWastes(final Continuation<? super List<Waste>> $completion) {
    final String _sql = "SELECT * FROM waste";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Waste>>() {
      @Override
      @NonNull
      public List<Waste> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfWaste = CursorUtil.getColumnIndexOrThrow(_cursor, "waste");
          final int _cursorIndexOfReuse = CursorUtil.getColumnIndexOrThrow(_cursor, "reuse");
          final int _cursorIndexOfSteps = CursorUtil.getColumnIndexOrThrow(_cursor, "steps");
          final List<Waste> _result = new ArrayList<Waste>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Waste _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpWaste;
            _tmpWaste = _cursor.getString(_cursorIndexOfWaste);
            final String _tmpReuse;
            _tmpReuse = _cursor.getString(_cursorIndexOfReuse);
            final List<String> _tmpSteps;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfSteps);
            _tmpSteps = __converters.toStringList(_tmp);
            _item = new Waste(_tmpId,_tmpWaste,_tmpReuse,_tmpSteps);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllTerraceFarming(final Continuation<? super List<TerraceFarming>> $completion) {
    final String _sql = "SELECT * FROM terrace_farming";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<TerraceFarming>>() {
      @Override
      @NonNull
      public List<TerraceFarming> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCrop = CursorUtil.getColumnIndexOrThrow(_cursor, "crop");
          final int _cursorIndexOfSunlight = CursorUtil.getColumnIndexOrThrow(_cursor, "sunlight");
          final int _cursorIndexOfWater = CursorUtil.getColumnIndexOrThrow(_cursor, "water");
          final int _cursorIndexOfDays = CursorUtil.getColumnIndexOrThrow(_cursor, "days");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfContainerSize = CursorUtil.getColumnIndexOrThrow(_cursor, "containerSize");
          final int _cursorIndexOfEmoji = CursorUtil.getColumnIndexOrThrow(_cursor, "emoji");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfTips = CursorUtil.getColumnIndexOrThrow(_cursor, "tips");
          final List<TerraceFarming> _result = new ArrayList<TerraceFarming>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final TerraceFarming _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpCrop;
            _tmpCrop = _cursor.getString(_cursorIndexOfCrop);
            final String _tmpSunlight;
            _tmpSunlight = _cursor.getString(_cursorIndexOfSunlight);
            final String _tmpWater;
            _tmpWater = _cursor.getString(_cursorIndexOfWater);
            final int _tmpDays;
            _tmpDays = _cursor.getInt(_cursorIndexOfDays);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpContainerSize;
            _tmpContainerSize = _cursor.getString(_cursorIndexOfContainerSize);
            final String _tmpEmoji;
            _tmpEmoji = _cursor.getString(_cursorIndexOfEmoji);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final List<String> _tmpTips;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfTips);
            _tmpTips = __converters.toStringList(_tmp);
            _item = new TerraceFarming(_tmpId,_tmpCrop,_tmpSunlight,_tmpWater,_tmpDays,_tmpDifficulty,_tmpContainerSize,_tmpEmoji,_tmpDescription,_tmpTips);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllSchemes(final Continuation<? super List<Scheme>> $completion) {
    final String _sql = "SELECT * FROM scheme";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Scheme>>() {
      @Override
      @NonNull
      public List<Scheme> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfBenefit = CursorUtil.getColumnIndexOrThrow(_cursor, "benefit");
          final int _cursorIndexOfEligibility = CursorUtil.getColumnIndexOrThrow(_cursor, "eligibility");
          final List<Scheme> _result = new ArrayList<Scheme>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Scheme _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final String _tmpBenefit;
            _tmpBenefit = _cursor.getString(_cursorIndexOfBenefit);
            final String _tmpEligibility;
            _tmpEligibility = _cursor.getString(_cursorIndexOfEligibility);
            _item = new Scheme(_tmpId,_tmpName,_tmpBenefit,_tmpEligibility);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
