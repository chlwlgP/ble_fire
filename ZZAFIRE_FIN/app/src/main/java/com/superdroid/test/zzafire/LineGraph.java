package com.superdroid.test.zzafire;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

public class LineGraph {
	private GraphicalView view;

	private TimeSeries dataset = new TimeSeries("HR"); //new data
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset(); //TimeSeries들의 리스트

	private XYSeriesRenderer renderer = new XYSeriesRenderer(); // 그래프의 속성(색, 크기)
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer(); //XYSeriesRenderer의 리스트

	// ChartFactory로 넘겨줌

	public LineGraph() {
		//Add single dataset to multiple dataset
		//add time series data to xy multi data
		mDataset.addSeries(dataset);

		// XYSeries들의 리스트
      /* 다른 그래프를 추가하고 싶으면 XYSeries를 추가로 생성한 후, dataset.addSeries(series) 해준다*/
		//custom renderer
		renderer.setColor(Color.RED);
		renderer.setPointStyle(PointStyle.SQUARE);
		renderer.setFillPoints(true);
		renderer.setDisplayChartValues(true);
		renderer.setChartValuesTextAlign(Paint.Align.RIGHT);
		renderer.setChartValuesTextSize(40);
		renderer.setLineWidth(5.0f);

		//Enable zoom on multi xy renderer
		mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBackgroundColor(Color.TRANSPARENT);
		mRenderer.setZoomButtonsVisible(true);
		mRenderer.setXTitle("Day #");
		mRenderer.setYTitle("Data");
		mRenderer.setAxisTitleTextSize(60);
		mRenderer.setShowGrid(true);
		mRenderer.setFitLegend(true);
		mRenderer.setGridColor(Color.DKGRAY);
		//mRenderer.addXTextLabel(p.getX(), p.getY())

		//Add single renderer to multiple renderer
		mRenderer.addSeriesRenderer(renderer);
	}

	public GraphicalView getView(Context context)
	{
		// XYSeriesRenderer들의 리스트(를 만들어 ChartFactory에 넘겨주면 그래프그림)
		//create graphical view
		view = ChartFactory.getLineChartView(context, mDataset, mRenderer);
		return view;
	}
	//add new points to data
	public void addNewPoints(Point p) {
		dataset.add(p.getX(), p.getY());
	}
}