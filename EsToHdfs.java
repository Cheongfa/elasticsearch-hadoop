package cn.edu.zju.elastisearch
import java.io.IOException;  
  
import org.apache.hadoop.conf.Configuration;  
import org.apache.hadoop.fs.Path;  
import org.apache.hadoop.io.Text;  
import org.apache.hadoop.mapreduce.Job;  
import org.apache.hadoop.mapreduce.Mapper;  
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;  
import org.apache.hadoop.util.GenericOptionsParser;  
import org.elasticsearch.hadoop.mr.EsInputFormat;  
import org.elasticsearch.hadoop.mr.LinkedMapWritable;  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
  
public class EsToHdfs {  
      
    private static Logger LOG = LoggerFactory.getLogger(EsToHdfs.class);  
  
    public static void main(String args[]) {  
        try {  
            Configuration conf = new Configuration();  
            conf.setBoolean("mapreduce.map.speculative", false);   
            conf.setBoolean("mapreduce.reduce.speculative", false);   
          
            conf.set("es.nodes", "master:9200");  
            //ElaticSearch Index/Type  
            conf.set("es.resource", "zju/cst");   
            String[] oArgs = new GenericOptionsParser(conf, args).getRemainingArgs();  
            if (oArgs.length != 1) {  
                LOG.error("error");  
                System.exit(2);  
            }  
            Job job = Job.getInstance(conf, "EsToHdfs");  
            job.setJarByClass(EsToHdfs.class);  
            job.setInputFormatClass(EsInputFormat.class);  
            job.setMapperClass(EsToHdfsMapper.class);  
            job.setMapOutputKeyClass(Text.class);  
            job.setMapOutputValueClass(LinkedMapWritable.class);  
              
            FileOutputFormat.setOutputPath(job, new Path(oArgs[0]));  
              
            System.out.println(job.waitForCompletion(true));  
        } catch (Exception e) {  
            LOG.error(e.getMessage(), e);  
        }  
    }  
      
}  
  
class EsToHdfsMapper extends Mapper<Text, LinkedMapWritable, Text, LinkedMapWritable> {  
  
    private static final Logger LOG = LoggerFactory.getLogger(E2HMapper01.class);  
      
    @Override  
    protected void setup(Context context) throws IOException, InterruptedException {  
        super.setup(context);  
    }  
  
    @Override  
    protected void map(Text key, LinkedMapWritable value, Context context)  
            throws IOException, InterruptedException {  
        LOG.info("key {} value {}", key, value);  
        context.write(key, value);  
    }  
      
    @Override  
    protected void cleanup(Context context) throws IOException, InterruptedException {  
        super.cleanup(context);  
    }  
      
}  
