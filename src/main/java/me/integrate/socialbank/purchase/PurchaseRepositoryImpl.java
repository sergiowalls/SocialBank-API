package me.integrate.socialbank.purchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {
    private final static String PACKAGE_TABLE = "\"package\"";
    private final static String PURCHASE_TABLE = "\"purchase\"";
    private final static String NAME = "name";
    private final static String PRICE = "price";
    private final static String HOURS = "hours";
    private final static String PACKAGE_NAME = "package_name";
    private final static String USER_EMAIL = "user_email";
    private final static String TRANSACTION_ID = "transaction_id";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public PurchaseRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HoursPackage getPackageByName(String packageName) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM " + PACKAGE_TABLE + " WHERE " + NAME + " = ?",
                    new Object[]{packageName}, new PackageRowMapper());
        } catch (EmptyResultDataAccessException ex) {
            throw new PackageNotFoundException();
        }
    }

    @Override
    public void storePurchase(HoursPackage hoursPackage, String email, String transactionID) {
        try {
            jdbcTemplate.update("INSERT INTO " + PURCHASE_TABLE + " VALUES (?, ?, ?)",
                    transactionID, email, hoursPackage.getName());
        } catch (DuplicateKeyException ex) {
            throw new TransactionAlreadyExistsException();
        }
    }

    private class PackageRowMapper implements RowMapper<HoursPackage> {

        @Override
        public HoursPackage mapRow(ResultSet resultSet, int i) throws SQLException {
            HoursPackage hoursPackage = new HoursPackage();
            hoursPackage.setName(resultSet.getString(NAME));
            hoursPackage.setHours(resultSet.getInt(HOURS));
            hoursPackage.setPrice(resultSet.getDouble(PRICE));

            return hoursPackage;
        }
    }
}
