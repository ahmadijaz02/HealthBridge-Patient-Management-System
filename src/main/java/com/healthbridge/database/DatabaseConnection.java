package com.healthbridge.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Database Connection class - simulates database connectivity
 * This class demonstrates another inter-class component
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private String connectionString;
    private String username;
    private String password;
    private boolean isConnected;
    private int connectionPoolSize;
    private Map<String, String> queryCache;
    private long lastConnectionTime;

    private DatabaseConnection() {
        this.queryCache = new HashMap<>();
        this.connectionPoolSize = 10;
    }

    /**
     * Singleton pattern for database connection
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize database connection
     */
    public void initialize(String connectionString, String username, String password) {
        this.connectionString = connectionString;
        this.username = username;
        this.password = password;
        
        System.out.println("Initializing database connection: " + connectionString);
    }

    /**
     * Connect to database
     */
    public boolean connect() {
        if (connectionString == null || connectionString.isEmpty()) {
            System.out.println("ERROR: Connection string not set");
            return false;
        }
        
        try {
            System.out.println("Connecting to database: " + connectionString);
            Thread.sleep(500); // Simulate connection delay
            
            isConnected = true;
            lastConnectionTime = System.currentTimeMillis();
            System.out.println("Successfully connected to database");
            return true;
        } catch (InterruptedException e) {
            System.out.println("ERROR: Connection interrupted");
            isConnected = false;
            return false;
        }
    }

    /**
     * Disconnect from database
     */
    public boolean disconnect() {
        if (!isConnected) {
            System.out.println("WARNING: Already disconnected");
            return false;
        }
        
        System.out.println("Disconnecting from database");
        isConnected = false;
        queryCache.clear();
        return true;
    }

    /**
     * Execute query
     */
    public String executeQuery(String query) {
        if (!isConnected) {
            System.out.println("ERROR: Database not connected");
            return null;
        }
        
        // SMELL: Simulate query caching without proper invalidation
        if (queryCache.containsKey(query)) {
            System.out.println("Query result retrieved from cache");
            return queryCache.get(query);
        }
        
        String result = "Query executed: " + query;
        queryCache.put(query, result);
        
        System.out.println("Query executed: " + query.substring(0, Math.min(50, query.length())));
        return result;
    }

    /**
     * Execute update
     */
    public int executeUpdate(String updateQuery) {
        if (!isConnected) {
            System.out.println("ERROR: Database not connected");
            return 0;
        }
        
        // SMELL: Cache invalidation not properly handled
        queryCache.clear(); // Naive approach to clear cache
        
        System.out.println("Update executed: " + updateQuery.substring(0, Math.min(50, updateQuery.length())));
        return 1; // Simulate one row affected
    }

    /**
     * Get connection status
     */
    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Get connection string
     */
    public String getConnectionString() {
        return connectionString;
    }

    /**
     * Get last connection time
     */
    public long getLastConnectionTime() {
        return lastConnectionTime;
    }

    /**
     * Get query cache size
     */
    public int getQueryCacheSize() {
        return queryCache.size();
    }

    /**
     * Clear query cache
     */
    public void clearQueryCache() {
        queryCache.clear();
        System.out.println("Query cache cleared");
    }

    /**
     * Get connection pool size
     */
    public int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    /**
     * Set connection pool size
     */
    public void setConnectionPoolSize(int poolSize) {
        this.connectionPoolSize = poolSize;
    }

    /**
     * Validate connection
     */
    public boolean validateConnection() {
        if (!isConnected) {
            return false;
        }
        
        long timeSinceLastConnection = System.currentTimeMillis() - lastConnectionTime;
        // SMELL: Hard-coded timeout value
        if (timeSinceLastConnection > 3600000) { // 1 hour
            System.out.println("WARNING: Connection idle for more than 1 hour");
            return false;
        }
        
        return true;
    }

    /**
     * Get database statistics
     */
    public Map<String, Object> getDatabaseStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("isConnected", isConnected);
        stats.put("connectionString", connectionString);
        stats.put("queryCacheSize", queryCache.size());
        stats.put("connectionPoolSize", connectionPoolSize);
        stats.put("lastConnectionTime", lastConnectionTime);
        return stats;
    }
}
