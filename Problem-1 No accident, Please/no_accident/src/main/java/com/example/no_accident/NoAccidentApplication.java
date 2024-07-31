package com.example.no_accident;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.*;

@SpringBootApplication
public class NoAccidentApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(NoAccidentApplication.class, args);
		printPaths();
	}
	public static void printPaths() throws Exception {
		Scanner scanner = new Scanner(System.in);

		// Read input for Flight 1
		System.out.println("Enter coordinates for Flight 1 (format: x y, enter 'done' to finish):");
		List<int[]> flight1 = readCoordinates(scanner);

		// Read input for Flight 2
		System.out.println("Enter coordinates for Flight 2 (format: x y, enter 'done' to finish):");
		List<int[]> flight2 = readCoordinates(scanner);

		// Read input for Flight 3
		System.out.println("Enter coordinates for Flight 3 (format: x y, enter 'done' to finish):");
		List<int[]> flight3 = readCoordinates(scanner);

		// Track coordinates occupied by flights
		Set<String> occupiedByFlight1 = new HashSet<>();
		Set<String> occupiedByFlight2 = new HashSet<>();
		Set<String> occupiedByFlight3 = new HashSet<>();

		// Track coordinates and segments occupied by flights
		Set<String> occupiedCoordinates = new HashSet<>();
		Set<String> occupiedSegments = new HashSet<>();

		// Process Flight 1
		List<int[]> adjustedFlight1 = processFlight(flight1, occupiedCoordinates, occupiedSegments);

		// Process Flight 2
		List<int[]> adjustedFlight2 = processFlight(flight2, occupiedCoordinates, occupiedSegments);

		// Process Flight 3
		List<int[]> adjustedFlight3 = processFlight(flight3, occupiedCoordinates, occupiedSegments);

		// Create dataset
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(createXYSeries("Flight 1", adjustedFlight1));
		dataset.addSeries(createXYSeries("Flight 2", adjustedFlight2));
		dataset.addSeries(createXYSeries("Flight 3", adjustedFlight3));

		// Create chart
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Flight Paths",
				"X",
				"Y",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);

		// Save chart as PNG image
		File imageFile = new File("FlightPathsChart.png");
		ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);

		System.out.println("Flight path chart saved as FlightPathsChart.png");
	}

	private static List<int[]> readCoordinates(Scanner scanner) {
		List<int[]> coordinates = new ArrayList<>();
		while (true) {
			String input = scanner.nextLine();
			if (input.equalsIgnoreCase("done")) {
				break;
			}
			String[] parts = input.split(" ");
			if (parts.length == 2) {
				try {
					int x = Integer.parseInt(parts[0]);
					int y = Integer.parseInt(parts[1]);
					coordinates.add(new int[]{x, y});
				} catch (NumberFormatException e) {
					System.out.println("Invalid input, please enter coordinates in format: x y");
				}
			} else {
				System.out.println("Invalid input, please enter coordinates in format: x y");
			}
		}
		return coordinates;
	}

	private static List<int[]> processFlight(List<int[]> flight, Set<String> occupiedCoordinates, Set<String> occupiedSegments) {
		List<int[]> adjustedFlight = new ArrayList<>();
		int[] start = flight.get(0);
		adjustedFlight.add(start);
		occupiedCoordinates.add(start[0] + "," + start[1]);

		for (int i = 1; i < flight.size(); i++) {
			int[] next = flight.get(i);
			int[] previous = adjustedFlight.get(i - 1);

			int x = previous[0];
			int y = previous[1];
			while (x != next[0] || y != next[1]) {
				if (x < next[0]) x++;
				else if (x > next[0]) x--;
				else if (y < next[1]) y++;
				else if (y > next[1]) y--;

				String currentSegment = previous[0] + "," + previous[1] + "-" + x + "," + y;
				if (occupiedSegments.contains(currentSegment)) {
					// Adjust the path if needed
					while (occupiedCoordinates.contains(x + "," + y) || occupiedSegments.contains(currentSegment)) {
						if (y < next[1] || y == previous[1]) y++;
						else if (y > next[1]) y--;
						else if (x < next[0] || x == previous[0]) x++;
						else if (x > next[0]) x--;
						currentSegment = previous[0] + "," + previous[1] + "-" + x + "," + y;
					}
				}

				adjustedFlight.add(new int[]{x, y});
				occupiedCoordinates.add(x + "," + y);
				occupiedSegments.add(currentSegment);
				previous = new int[]{x, y};
			}

			adjustedFlight.add(next);
			occupiedCoordinates.add(next[0] + "," + next[1]);
		}

		return adjustedFlight;
	}
	private static XYSeries createXYSeries(String label, List<int[]> path) {
		XYSeries series = new XYSeries(label);
		for (int[] coord : path) {
			series.add(coord[0], coord[1]);
		}
		return series;
	}
}
