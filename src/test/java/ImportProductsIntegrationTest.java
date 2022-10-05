import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/import-products-job-context.xml", "/test-context.xml"})
public class ImportProductsIntegrationTest {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Cleans and populates DB
     */
    @Before
    public void setUp() throws Exception {
        jdbcTemplate.update("delete from product");
        jdbcTemplate.update("insert into product (id,name,description,price) values(?,?,?,?)",
                "PR....214", "Nokia 2610 Phone", "", 102.23);
    }

    @Test
    public void importProducts() throws Exception {
        int initial = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);
        // Launches job with parameters
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("inputResource", "classpath:/input/products.zip")
                .addString("targetDirectory", "./target/importproductsbatch/")
                .addString("targetFile", "products.txt")
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters()
        );
        int nbOfNewProducts = 7;
        int eventual = jdbcTemplate.queryForObject("select count(1) from product", Integer.class);
        // Checks correct item insertion
        Assert.assertEquals(initial + nbOfNewProducts, eventual);
    }
}
